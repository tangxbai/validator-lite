/**
 * Copyright (C) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.validator;

import static com.viiyue.plugins.validator.Validator.LOG;
import static com.viiyue.plugins.validator.common.Constants.DEFAULT_MESSAGE_KEY_PREFIX;
import static com.viiyue.plugins.validator.common.Constants.DEFAULT_MESSAGE_LANGUAGES;
import static com.viiyue.plugins.validator.common.Constants.DEFAULT_RESOURCE_NAME;
import static com.viiyue.plugins.validator.common.Constants.EXPRESSION_TOKEN_BEGIN;
import static com.viiyue.plugins.validator.common.Constants.EXPRESSION_TOKEN_END;
import static com.viiyue.plugins.validator.common.Constants.MESSAGE_KEY_MISSING_VALUE;
import static com.viiyue.plugins.validator.common.Constants.MESSAGE_KEY_TEST_PASSED;
import static com.viiyue.plugins.validator.common.Constants.MESSAGE_KEY_TEST_REJECTED;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.annotation.Label;
import com.viiyue.plugins.validator.annotation.When;
import com.viiyue.plugins.validator.annotation.When.Result;
import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.handler.BaseHandler;
import com.viiyue.plugins.validator.handler.BoundaryHandler;
import com.viiyue.plugins.validator.handler.CommonHanlder;
import com.viiyue.plugins.validator.handler.ContainsHandler;
import com.viiyue.plugins.validator.handler.EqualsHandler;
import com.viiyue.plugins.validator.handler.Handler;
import com.viiyue.plugins.validator.handler.LengthHandler;
import com.viiyue.plugins.validator.handler.MaxHandler;
import com.viiyue.plugins.validator.handler.MinHandler;
import com.viiyue.plugins.validator.handler.NotBlankHandler;
import com.viiyue.plugins.validator.handler.NotEmptyHandler;
import com.viiyue.plugins.validator.handler.PasswordHandler;
import com.viiyue.plugins.validator.handler.PatternHandler;
import com.viiyue.plugins.validator.handler.RangeHandler;
import com.viiyue.plugins.validator.handler.RequiredHandler;
import com.viiyue.plugins.validator.handler.URLHandler;
import com.viiyue.plugins.validator.metadata.Conditional;
import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.metadata.result.ElementResult;
import com.viiyue.plugins.validator.metadata.result.FragmentResult;
import com.viiyue.plugins.validator.metadata.result.ValidatedResult;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.scripting.ExpressionResolver;
import com.viiyue.plugins.validator.scripting.compiler.ElementCompiler;
import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;
import com.viiyue.plugins.validator.scripting.message.DefaultMessageResolver;
import com.viiyue.plugins.validator.scripting.message.MessageResolver;
import com.viiyue.plugins.validator.utils.ArrayUtil;
import com.viiyue.plugins.validator.utils.Assert;
import com.viiyue.plugins.validator.utils.BeanUtil;
import com.viiyue.plugins.validator.utils.ClassUtil;
import com.viiyue.plugins.validator.utils.TextUtil;

/**
 * The default validation factory implementation class provides some basic
 * validation capabilities.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class ValidatorFactoryProvider implements ValidatorFactory {

	private MessageResolver messageResolver;
	private ContextConfigurion configuration;

	private final ElementCompiler compiler = new ElementCompiler();
	private final ConcurrentMap<String, Handler> handlers = new ConcurrentHashMap<String, Handler>( 64 );
	
	private static class Holder {
		private static final ValidatorFactoryProvider INSTANCE = new ValidatorFactoryProvider();
	}
	
	public static final ValidatorFactory getInstance() {
		return Holder.INSTANCE;
	}
	
	public ValidatorFactoryProvider() {
		loadHandlers();
	}
	
	@Override
	public List<Fragment> compile( String rules ) {
		return compiler.compile( rules );
	}

	@Override
	public List<Fragment> compile( Parameter parameter ) {
		return compiler.compile( parameter );
	}
	
	@Override
	public List<Element> compile( Class<?> beanType ) {
		return compiler.compile( beanType );
	}
	
	@Override
	public ContextConfigurion getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration( ContextConfigurion configuration ) {
		Assert.isNull( this.configuration, "Context configuration allows configuration only once" );
		this.configuration = configuration == null ? new ContextConfigurion() : configuration;
		this.compiler.getTemplateParser().setStrictMode( this.configuration.isEnableStrictMode() );
		this.initMessageResovler( getMessageResolver() );
	}

	@Override
	public MessageResolver getMessageResolver() {
		if ( messageResolver == null ) {
			this.messageResolver = new DefaultMessageResolver( DEFAULT_RESOURCE_NAME, DEFAULT_MESSAGE_KEY_PREFIX, DEFAULT_MESSAGE_LANGUAGES );
		}
		return messageResolver;
	}
	
	@Override
	public void setMessageResolver( MessageResolver messageResolver ) {
		if ( this.messageResolver == null ) {
			this.messageResolver = messageResolver;
		}
	}
	
	@Override
	public String getResourceMessage( String message, Locale locale ) {
		Assert.notNull( message, "'message' cannot be null" );
		if ( TextUtil.isWrappedWith( message, EXPRESSION_TOKEN_BEGIN, EXPRESSION_TOKEN_END ) ) {
			String messageKey = TextUtil.trimWrapper( message, EXPRESSION_TOKEN_BEGIN, EXPRESSION_TOKEN_END );
			String messageValue = getMessageResolver().resolve( messageKey, locale );
			if ( messageValue != null ) {
				message = messageValue; 
			}
		}
		return message;
	}
	
	@Override
	public String getResourceMessage( Context context, Fragment fragment ) {
		if ( StringUtils.isNotEmpty( fragment.getMessage() ) ) {
			String resolvedMessage = getResourceMessage( fragment.getMessage(), context.getLocale() );
			return ExpressionResolver.resolveResourceText( resolvedMessage, context, fragment.getArguments() );
		}
		
		MessageResolver messageResolver = getMessageResolver();
		String [] messageKeys = context.getMessageKeys();
		if ( ArrayUtil.isEmpty( messageKeys ) ) {
			messageKeys = new String[] { fragment.getName() };
		}
		
		for ( String messageKey : messageKeys ) {
			String errorCode = messageResolver.getMessageKey( messageKey );
			String errorMessage = messageResolver.resolve( errorCode, context.getLocale() );
			if ( errorMessage != null && !Objects.equals( errorCode, errorMessage ) ) {
				errorMessage = ExpressionResolver.resolveResourceText( errorMessage, context, fragment.getArguments() );
				return errorMessage;
			}
		}
		return "{" + messageResolver.getMessageKey( messageKeys[ 0 ] ) + "}";
	}

	@Override
	public void addHandler( String className ) {
		Assert.notNull( className, "Handler class name cannot be null" );
		Class<?> clazz = ClassUtil.forName( className );
		Assert.isAssignable( clazz, Handler.class, "Handler <{0}> must be of type <{1}>", className, Handler.class.getName() );
		addHandler( ( Class<? extends Handler> ) clazz );
	}

	@Override
	public void addHandler( Class<? extends Handler> handlerType ) {
		Assert.notNull( handlerType, "Handler class type cannot be null" );
		addHandler( BeanUtil.newInstance( handlerType ) );
	}

	@Override
	public void addHandler( Handler handler ) {
		if ( handler != null ) {
			if ( handler instanceof BaseHandler ) {
				for ( String name : ( ( BaseHandler ) handler ).names() ) {
					registerHandler( name, handler );
				}
			} else {
				registerHandler( handler.name(), handler );
			}
		}
	}

	@Override
	public Map<String, Handler> getHandlers() {
		return Collections.unmodifiableMap( handlers );
	}

	@Override
	public ValidatedResult validateValue( Object value, String rules, String labelText, Locale locale, Class<?> ... groups ) {
		ValidatedResult testResult = new ValidatedResult();
		List<Fragment> fragments = compiler.compile( rules );
		List<FragmentResult> results = doValidateValue( null, value, null, fragments, locale, groups );
		if ( results != null ) {
			testResult.addRejectedResult( createElementResult( value, results, "target", labelText, locale ) );
		} else {
			testResult.passedAccumulation();
		}
		return changeResult( testResult, locale );
	}
	
	@Override
	public ValidatedResult validateParameter( Object value, Parameter parameter, String labelText, Locale locale, Class<?> ... groups ) {
		ValidatedResult testResult = new ValidatedResult();
		List<Fragment> fragments = compiler.compile( parameter );
		List<FragmentResult> results = doValidateValue( null, value, null, fragments, locale, groups );
		if ( results != null ) {
			Label label = parameter.getAnnotation( Label.class );
			labelText = label == null || StringUtils.isEmpty( label.value() ) ? labelText : label.value();
			testResult.addRejectedResult( createElementResult( value, results, parameter.getName(), labelText, locale ) );
		} else {
			testResult.passedAccumulation();
		}
		return changeResult( testResult, locale );
	}

	@Override
	public ValidatedResult validateBean( Object bean, Locale locale, Class<?> ... groups ) {
		MessageResolver messageResolver = getMessageResolver();
		
		// If the validation object is null, the validation is passed directly.
		if ( bean == null ) {
			String messageKey = messageResolver.getMessageKey( MESSAGE_KEY_MISSING_VALUE );
			return ValidatedResult.empty( messageResolver.resolve( messageKey, locale ) );
		}
		
		// Get the list of compiled elements. 
		// If you have compiled in advance, you should get the results directly, 
		// otherwise it will take some time to compile the validation rules.
		final Class<?> beanType = bean.getClass();
		final List<Element> elements = compiler.compile( beanType );
		
		// If the list of validation rules is empty, the validation is also passed.
		if ( CollectionUtils.isEmpty( elements ) ) {
			String messageKey = messageResolver.getMessageKey( MESSAGE_KEY_MISSING_VALUE );
			return ValidatedResult.empty( messageResolver.resolve( messageKey, locale ) );
		}
		
		// Initial validation results
		final int elementSize = elements.size();
		final ValidatedResult testResult = new ValidatedResult();
		testResult.setTotalCount( elementSize );
		
		// Validating unconditional elements
		List<Element> conditionalElements = null;
		Map<String, When.Result> testedMapping = new HashMap<String, When.Result>( elementSize / 2 );
		for ( Element element : elements ) {
			if ( element.isUnconditional() ) {
				if ( doValidateElement( testResult, bean, element, locale, groups ) ) {
					testedMapping.put( element.getProperty(), When.Result.PASSED );
				} else {
					testedMapping.put( element.getProperty(), When.Result.REJECTED );
					if ( configuration.isEnableSingleMode() ) {
						return changeResult( testResult, locale );
					}
				}
			} else {
				if ( conditionalElements == null ) {
					// Make predictions only, not actual initial capacity...
					conditionalElements = new ArrayList<Element>( elementSize - testedMapping.size() );
				}
				conditionalElements.add( element );
			}
		}
		
		// Selective condition judgment only supports simple nesting, and does
		// not support mutual nesting.
		if ( conditionalElements != null ) {
			for ( Element element : conditionalElements ) {
				if ( containsResult( testedMapping, element.getConditional() ) ) {
					if ( !doValidateElement( testResult, bean, element, locale, groups ) && configuration.isEnableSingleMode() ) {
						return changeResult( testResult, locale );
					}
				} else {
					testResult.ignoredAccumulation();
				}
			}
		}
		return changeResult( testResult, locale );
	}
	
	private boolean doValidateElement( ValidatedResult testResult, Object bean, Element element, Locale locale, Class<?> ... groups ) {
		boolean isPassed = true;
		final String property = element.getProperty();
		final Object elementValue = element.getValue( bean );
		final List<Fragment> fragments = element.getFragments();

		ElementResult result = null;
		if ( CollectionUtils.isNotEmpty( fragments ) ) {
			List<FragmentResult> results = doValidateValue( bean, elementValue, element, fragments, locale, groups );
			if ( results != null ) {
				isPassed = false;
				result = createElementResult( elementValue, results, property, element.getLabel(), locale );
			}
		}
		if ( element.isJavaBean() && result == null ) {
			ValidatedResult beanResult = validateBean( elementValue, locale, groups );
			result = createElementResult( elementValue, beanResult, property, element.getLabel(), locale );
			result.setJavaBean( element.isJavaBean() );
			isPassed = beanResult.isPassed();
		}
		if ( isPassed ) {
			testResult.passedAccumulation();
		} else {
			testResult.addRejectedResult( result );
		}
		return isPassed;
	}
	
	private List<FragmentResult> doValidateValue( Object bean, Object value, Element element, List<Fragment> fragments, Locale locale, Class<?> ... groups ) {
		if ( CollectionUtils.isEmpty( fragments ) ) {
			return null; // Passed
		}
		fragments = getFragmentsByGroup( fragments, groups );
		if ( CollectionUtils.isEmpty( fragments ) ) {
			return null; // Passed
		}
		List<FragmentResult> results = null;
		for ( final Fragment fragment : fragments ) {
			String fragmentName = fragment.getName();
			Context context = new Context( this, element, bean, locale );
			if ( !doFragmentValidate( value, fragmentName, fragment, context ) ) {
				if ( results == null ) {
					results = new ArrayList<FragmentResult>( fragments.size() );
				}
				String errorCode = null;
				String errorMessage = getResourceMessage( context, fragment );
				
				String [] messageKeys = context.getMessageKeys();
				if ( messageKeys == null ) {
					errorCode = getMessageResolver().getMessageKey( fragment.getName() );
				} else {
					errorCode = messageKeys[ messageKeys.length - 1 ];
				}
				// errorMessage = TextUtil.uncapitalize( errorMessage );
				results.add( new FragmentResult( fragmentName, errorCode, errorMessage, fragment.getArguments() ) );
			}
		}
		return results;
	}
	
	private boolean doFragmentValidate( Object value, String fragmentName, Fragment frgament, Context context ) {
		final Handler handler = handlers.get( fragmentName );
		if ( handler == null ) {
			LOG.error( "Fragment \"{}\" did not find a suitable handler", fragmentName );
			return true;
		}
		if ( value != null ) {
			Class<?> valueType = value.getClass();
			if ( !handler.support( valueType ) ) {
				LOG.warn( "Handler \"{}\" cannot handle parameter of type \"{}\"", handler.getClass().getName(), valueType );
				return true;
			}
		}
		return handler.doHandle( value, frgament, context );
	}
	
	private List<Fragment> getFragmentsByGroup( List<Fragment> fragments, Class<?> [] groups ) {
		if ( ArrayUtils.isEmpty( groups ) || Objects.equals( Constants.DEFAULT_GROUP, groups[ 0 ] ) ) {
			return fragments;
		}
		List<Fragment> filteredFragments = new ArrayList<Fragment>( fragments.size() / 2 );
		for ( Class<?> group : groups ) {
			for ( Fragment fragment : fragments ) {
				if ( fragment.isInGroup( group ) ) {
					filteredFragments.add( fragment );
				}
			}
		}
		return filteredFragments;
	}
	
	private ElementResult createElementResult( Object value, Object results, String fieldName, String labelText, Locale locale ) {
		ElementResult result = new ElementResult();
		result.setResult( results );
		result.setField( fieldName );
		result.setFieldValue( value );
		result.setLabel( getResourceMessage( labelText, locale ) );
		return result;
	}
	
	private ValidatedResult changeResult( ValidatedResult result, Locale locale ) {
		MessageResolver messageResolver = getMessageResolver();
		String messageKey = messageResolver.getMessageKey( result.isPassed() ? MESSAGE_KEY_TEST_PASSED : MESSAGE_KEY_TEST_REJECTED );
		result.setMessage( messageResolver.resolve( messageKey, locale ) );
		return result;
	}
	
	private boolean containsResult( Map<String, When.Result> target, Conditional conditional ) {
		boolean contains = true;
		When.Result test = conditional.getTest();
		for ( String property : conditional.getProperties() ) {
			Result result = target.get( property );
			if ( result == null ) {
				LOG.warn( "Target condition judgment field \"{}\" does not exist", property );
				contains = false;
			} else if ( result != test ) {
				contains = false;
			}
		}
		return contains;
	}
	
	private void registerHandler( String handlerName, Handler handler ) {
		if ( this.handlers.containsKey( handlerName ) ) {
			LOG.warn( "The handler \"{}\" already exists, but you replaced it", handler.name() );
		}
		this.handlers.put( handlerName, handler );
	}
	
	private void initMessageResovler( MessageResolver messageResolver ) {
		messageResolver.setConfiguration( configuration );
		messageResolver.addResourceBundles( configuration.getResources() );
		messageResolver.addResourceBundle( DEFAULT_RESOURCE_NAME, DEFAULT_MESSAGE_LANGUAGES );
		messageResolver.setDefaultLocale( configuration.getDefaultLanguage() );
	}

	private void loadHandlers() {
		addHandler( new CommonHanlder() );
		addHandler( new ContainsHandler() ); // contains(?, ?, ?, ...)
		addHandler( new EqualsHandler() ); // equals(?)
		addHandler( new LengthHandler() ); // length(?)
		addHandler( new MaxHandler() ); // max(?)
		addHandler( new MinHandler() ); // min(?)
		addHandler( new NotBlankHandler() ); // not-blank
		addHandler( new NotEmptyHandler() ); // not-empty
		addHandler( new PasswordHandler() ); // password(?)
		addHandler( new PatternHandler() ); // pattern(/?/)
		addHandler( new RangeHandler() ); // range(?, ?)
		addHandler( new RequiredHandler() ); // required
		addHandler( new BoundaryHandler() ); // prefixs(?, ?, ?, ...) | suffixs(?, ?, ?, ...)
		addHandler( new URLHandler() ); // url(?, ?, ?)
	}
	
}

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

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.metadata.result.ValidatedResult;
import com.viiyue.plugins.validator.scripting.ExpressionResolver;
import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;
import com.viiyue.plugins.validator.scripting.message.MessageResolver;
import com.viiyue.plugins.validator.scripting.parser.TemplateRuleParser;

/**
 * Core validation class.
 * 
 * <p>
 * This class uses {@link ValidatorFactoryProvider} to implement the default
 * implementation of the validation interface {@link ValidatorFactory}. You can also
 * implement the validation logic in it yourself, and then use {@link Validator#initFactory(ValidatorFactory)} for
 * custom injection.
 * </p>
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Validator {
	
	private Validator() {}

	private static ValidatorFactory factory = null;
	public static final Logger LOG = LoggerFactory.getLogger( Validator.class );
	
	/**
	 * <p>
	 * In order to solve the problem that the direct access speed is very slow
	 * in extreme cases, manually call some methods here to let the VM
	 * initialize their resources.
	 * 
	 * <p><b>NOTE</b>: Force objects to initialize by reading them
	 */
	public static void prepare() {
		ExpressionResolver.prepare();
	}
	
	/**
	 * Implementation class for injecting custom validation factory
	 * {@link ValidatorFactory}
	 * 
	 * @param customFactory your custom validation implementation class object
	 */
	public static void initFactory( ValidatorFactory customFactory ) {
		if ( factory == null ) {
			synchronized ( Validator.class ) {
				if ( factory == null ) {
					factory = customFactory;
				}
			}
		}
	}
	
	/**
	 * Get the currently valid validation factory class. If no custom injection
	 * has been performed, use the default factory implementation provider
	 * {@link ValidatorFactoryProvider}.
	 *  
	 * @return data validation factory implementation class
	 */
	public static ValidatorFactory getFactory() {
		if ( factory == null ) {
			initFactory( ValidatorFactoryProvider.getInstance() );
		}
		return factory;
	}
	
	/**
	 * Change some optional configurations
	 * 
	 * @param configuration the context Configuration Object
	 */
	public static void configuration( ContextConfigurion configuration ) {
		getFactory().setConfiguration( configuration );
	}
	
	/**
	 * Replace the default internationalized message resolver
	 * 
	 * @param messageResolver the used to replace the default message resolver
	 */
	public static void setMessageResolver( MessageResolver messageResolver ) {
		getFactory().setMessageResolver( messageResolver );
	}
	
	/**
	 * Compile validation rule constraints
	 * 
	 * @param rules the validation rule constraints
	 * @return the list of compiled rule fragments
	 * 
	 * @see TemplateRuleParser
	 * @see Fragment
	 */
	public static List<Fragment> compile( String rules ) {
		return getFactory().compile( rules );
	}
	
	/**
	 * Compile rule constraints for specified parameters
	 * 
	 * @param parameter the specified validation parameter
	 * @return the list of compiled rule fragments
	 * 
	 * @see Fragment
	 */
	public static List<Fragment> compile( Parameter parameter ) {
		return getFactory().compile( parameter );
	}
	
	/**
	 * Compile the specified java validation constraint rules
	 * 
	 * @param beanType the target bean class type
	 * @return the list of compiled rule elements
	 * 
	 * @see Element
	 */
	public static List<Element> compile( Class<?> beanType ) {
		return getFactory().compile( beanType );
	}
	
	/**
	 * <p>
	 * Validate the constraints of the specified bean.
	 * 
	 * <p>
	 * This method does not explicitly specify the locale. If no default locale
	 * is configured, the default locale of the current system will be used.
	 * 
	 * <p>
	 * NOTE: If you want to explicitly specify that text is output in a certain
	 * locale, use {@link #validateBean(Object, Locale, Class...)}
	 * 
	 * @param bean the target java bean object to be validated
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 * 
	 * @see ContextConfigurion#setDefaultLocale(Locale)
	 * @see ContextConfigurion#setDefaultLanguage(String)
	 * @see Locale#getDefault()
	 */
	public static ValidatedResult validateBean( Object bean, Class<?> ... groups ) {
		return getFactory().validateBean( bean, null, groups );
	}
	
	/**
	 * <p>
	 * Validate the constraints of the specified bean.
	 * 
	 * <p>
	 * This method can explicitly specify which locale is used for message
	 * output.
	 * 
	 * @param bean the target java bean object to be validated
	 * @param locale the specified locale
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 */
	public static ValidatedResult validateBean( Object bean, Locale locale, Class<?> ... groups ) {
		return getFactory().validateBean( bean, locale, groups );
	}
	
	/**
	 * <p>
	 * Validate specified value using constraint rule group.
	 * 
	 * <p>
	 * This method does not explicitly specify the locale. If no default locale
	 * is configured, the default locale of the current system will be used.
	 * 
	 * <p>
	 * NOTE: If you want to explicitly specify that text is output in a certain
	 * locale, use {@link #validateValue(Object, String, String, Locale, Class...)}.
	 * 
	 * @param value the target value to be validated
	 * @param rules the validation constraint rule combination
	 * @param message the default output message
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 */
	public static ValidatedResult validateValue( Object value, String rules, String message, Class<?> ... groups ) {
		return getFactory().validateValue( value, rules, message, null, groups );
	}
	
	/**
	 * Validate specified value using constraint rule group.
	 * 
	 * @param value the target value to be validated
	 * @param rules the validation constraint rule combination
	 * @param message the default output message
	 * @param locale the specified locale
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 */
	public static ValidatedResult validateValue( Object value, String rules, String message, Locale locale, Class<?> ... groups ) {
		return getFactory().validateValue( value, rules, message, locale, groups );
	}
	
	/**
	 * <p>
	 * Validate constraint rules for specified parameters.
	 * 
	 * <p>
	 * This method does not explicitly specify the locale. If no default locale
	 * is configured, the default locale of the current system will be used.
	 * 
	 * <p>
	 * NOTE: If you want to explicitly specify that text is output in a certain
	 * locale, use {@link #validateParameter(Object, Parameter, String, Locale, Class...)}
	 * 
	 * @param value the target value to be validated
	 * @param parameter the target parameter object
	 * @param message the default output message
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 */
	public static ValidatedResult validateParameter( Object value, Parameter parameter, String message, Class<?> ... groups ) {
		return getFactory().validateParameter( value, parameter, message, null, groups );
	}
	
	/**
	 * Validate constraint rules for specified parameters.
	 * 
	 * @param value the target value to be validated
	 * @param parameter the target parameter object
	 * @param message the default output message
	 * @param locale the specified locale
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 */
	public static ValidatedResult validateParameter( Object value, Parameter parameter, String message, Locale locale, Class<?> ... groups ) {
		return getFactory().validateParameter( value, parameter, message, locale, groups );
	}
	
}

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
import java.util.Map;

import com.viiyue.plugins.validator.exception.ArgumentException;
import com.viiyue.plugins.validator.exception.ReflectionException;
import com.viiyue.plugins.validator.handler.Handler;
import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.metadata.result.ValidatedResult;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;
import com.viiyue.plugins.validator.scripting.message.MessageResolver;
import com.viiyue.plugins.validator.scripting.parser.TemplateRuleParser;

/**
 * Validation plugin abstract interface, we will provide you with a default
 * validation factory implementation class, if you feel that the default
 * implementation class does not meet your needs, you can implement this class
 * by yourself to achieve your purpose.
 * 
 * <p>Default factory implementation class: {@link ValidatorFactoryProvider}
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 */
public interface ValidatorFactory {

	/**
	 * Optional configuration for setting context
	 * 
	 * @param configuration the context configuration
	 */
	void setConfiguration( ContextConfigurion configuration );
	
	/**
	 * @return the {@code ContextConfigurion} object
	 */
	ContextConfigurion getConfiguration();
	
	/**
	 * Specifies the international message resolver used by the validation factory
	 * 
	 * @param messageResolver the internationalized message parser
	 */
	void setMessageResolver( MessageResolver messageResolver );
	
	/**
	 * You can use this object to get some validation messages
	 * 
	 * @return an internationalized message resolver
	 */
	MessageResolver getMessageResolver();
	
	/**
	 * <p>
	 * Get the internationalized text of the specified message text expression.
	 * 
	 * <p>
	 * If the given text is <code>{xx}</code>, it will be automatically parsed into the text
	 * corresponding to the key existing in the resource message. If it is not
	 * <code>{xx}</code>, the text will be returned directly.
	 * 
	 * @param message the given message text
	 * @param locale the specified locale
	 * @return parsed text
	 */
	String getResourceMessage( String message, Locale locale );
	
	/**
	 * Get the internationalized message text corresponding to the specified
	 * validation fragment.
	 * 
	 * @param context the validate fragment context object
	 * @param fragment the validation rule fragmentation
	 * @return parsed text
	 */
	String getResourceMessage( Context context, Fragment fragment );

	/**
	 * Register a handler instance by class name
	 * 
	 * @param className the handler class name
	 * @throws ArgumentException this exception is thrown when the handler type is null.
	 * @throws ReflectionException this exception is thrown when a handler type instantiation fails.
	 */
	void addHandler( String className );

	/**
	 * Register a handler instance by class type
	 * 
	 * @param handlerType the constraint handler class type
	 * @throws ArgumentException this exception is thrown when the handler type is null.
	 * @throws ReflectionException this exception is thrown when a handler type instantiation fails.
	 */
	void addHandler( Class<? extends Handler> handlerType );

	/**
	 * Register a validation constraint handler
	 * 
	 * @param handler validation rule handler
	 */
	void addHandler( Handler handler );

	/**
	 * @return all constraint handlers, the returned result cannot be operated for read only.
	 */
	Map<String, Handler> getHandlers();

	/**
	 * Compile validation rule constraints
	 * 
	 * @param rules the validation rule constraints
	 * @return the list of compiled rule fragments
	 * 
	 * @see TemplateRuleParser
	 * @see Fragment
	 */
	List<Fragment> compile( String rules );
	
	/**
	 * Compile rule constraints for specified parameters
	 * 
	 * @param parameter the specified validation parameter
	 * @return the list of compiled rule fragments
	 * 
	 * @see Fragment
	 */
	List<Fragment> compile( Parameter parameter );
	
	/**
	 * Compile the specified java validation constraint rules
	 * 
	 * @param beanType the target bean class type
	 * @return the list of compiled rule elements
	 * 
	 * @see Element
	 */
	List<Element> compile( Class<?> beanType );

	/**
	 * Validate the constraints of the specified bean.
	 * 
	 * @param bean the target java bean object to be validated
	 * @param locale the specified locale
	 * @param groups validate the constraint rules under the specified group
	 * @return the verified result
	 */
	ValidatedResult validateBean( Object bean, Locale locale, Class<?> ... groups );
	
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
	ValidatedResult validateValue( Object value, String rules, String message, Locale locale, Class<?> ... groups );
	
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
	ValidatedResult validateParameter( Object value, Parameter parameter, String message, Locale locale, Class<?> ... groups );

}

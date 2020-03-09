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
package com.viiyue.plugins.validator.handler;

import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.ConvertUtils;

import com.viiyue.plugins.validator.Validator;
import com.viiyue.plugins.validator.ValidatorFactory;
import com.viiyue.plugins.validator.constraints.Equals;
import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * Validates the annotated object value should be the same as the specified target value.
 * 
 * <p>Note: 
 * It can be specified as a field of the same name in JavaBean.
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see Equals
 */
public class EqualsHandler extends BaseHandler {

	public EqualsHandler() {
		super( "equals" );
		super.setArgumentNumbers( 1, 2 );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		Element element = context.getElement();
		
		// Reference value
		Object targetValue = fragment.getArgument( 0 );
		String targetStringValue = targetValue.toString();
		if ( targetStringValue.startsWith( "#" ) ) {
			
			if ( element == null ) {
				Validator.LOG.warn( "Reference comparisons are only available in Java beans, which always returns true here." );
				return true;
			}
			
			String property = targetStringValue.substring( 1 );
			ValidatorFactory factory = context.getFactory();
			Element targetEelemnt = getTargetElement( context, element, property );
			if ( targetEelemnt == null ) {
				Validator.LOG.warn( "{} field not found", property );
				return true;
			}
			
			Object propertyValue = targetEelemnt.getValue( context.getInstance() );
			if ( Objects.equals( value, propertyValue ) ) {
				return true;
			}
			
			setMessageKeys( context, fragment, "related" ); // {<?>.equals.related}
			String message = factory.getResourceMessage( targetEelemnt.getLabel(), context.getLocale() );
			context.set( "target", message );
			return false;
		}
		
		// String value
		Class<?> targetType = element.getFieldType();
		if ( Objects.equals( targetType, String.class ) && Objects.equals( value.toString(), targetStringValue ) ) {
			return true;
		}
		
		// Object value
		targetValue = ConvertUtils.lookup( targetType ).convert( targetType, targetValue );
		if ( Objects.equals( value, targetValue ) ) {
			return true;
		}
		
		setMessageKeys( context, fragment, "specify" ); // {<?>.equals.specify} 
		return false;
	}

	private Element getTargetElement( Context context, Element element, String property ) {
		ValidatorFactory factory = context.getFactory();
		List<Element> elements = factory.compile( element.getBeanType() );
		for ( Element ele : elements ) {
			if ( Objects.equals( property, ele.getProperty() ) ) {
				return ele;
			}
		}
		return null;
	}
	
}

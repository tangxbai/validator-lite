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

import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.exception.ValidatorException;
import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.utils.ArrayUtil;
import com.viiyue.plugins.validator.utils.Assert;
import com.viiyue.plugins.validator.utils.ClassUtil;

/**
 * Basic data validation handler. Generally,
 * the handler corresponding to the {@link Handler} interface can only have a
 * corresponding name, but if it inherits from {@link BaseHandler}, the handler can be
 * used to bind multiple names.
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see Handler
 */
public abstract class BaseHandler implements Handler {

	private final String[] names;
	private boolean varargs;
	private Range<Integer> argumentNumberRange;
	
	public BaseHandler( String ... names ) {
		Assert.isTrue( ArrayUtil.isNotEmpty( names ), "The handler must specify at least one name" );
		this.names = names;
	}
	
	/**
	 * Used to specify whether the target value is required. 
	 * The default is not required.
	 * 
	 * @return true if required, otherwise optional.
	 */
	public boolean required() {
		return false;
	}
	
	@Override
	public final String name() {
		return names[ 0 ]; // Useless
	}
	
	/**
	 * @return an array of names supporting validation fragments
	 */
	public final String [] names() {
		return names;
	}
	
	/**
	 * @return the supported data types
	 */
	public Class<?> [] supports() {
		return null;
	}
	
	@Override
	public boolean support( Class<?> valueType ) {
		if ( supports() == null ) {
			return true;
		}
		for ( Class<?> type : supports() ) {
			if ( ClassUtils.isAssignable( valueType, type ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean doHandle( Object value, Fragment fragment, Context context ) {
		doArgumentsCheck( fragment );
		boolean isValid = ( value == null ? !required() : doValidate( value, fragment, context ) );
		if ( !isValid && context.getMessageKeys() == null ) {
			setMessageKeys( context, fragment, null );
		}
		return isValid;
	}
	
	/**
	 * Set text keys for internationalized message text support
	 * 
	 * @param context the current validation context object
	 * @param fragment the fragment of current validation rule
	 * @param supplement the supplementary message text
	 */
	public void setMessageKeys( Context context, Fragment fragment, String supplement ) {
		String baseKey = fragment.getName();
		if ( StringUtils.isNotEmpty( supplement ) ) {
			baseKey += "." + supplement;
		}
		Element element = context.getElement();
		if ( element == null ) {
			context.setMessageKeys( baseKey );
		} else {
			Set<Class<?>> allTypes = ClassUtil.getAllSuperclasses( element.getFieldType() );
			int index = 0, size = allTypes.size();
			String [] messageKeys = new String[ size + 1 ];
			for ( Class<?> type : allTypes ) {
				messageKeys[ index ++ ] = baseKey + "." + type.getName();
			}
			messageKeys[ size ] = baseKey;
			context.setMessageKeys( messageKeys );
		}
	}
	
	/**
	 * Handle specific validation logic
	 * 
	 * @param value the target validation value
	 * @param fragment the fragment of current validation rule
	 * @param context the validation rule context object
	 * @return true if the validation is passed, otherwise the validation is rejected.
	 */
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		return true;
	}

	/**
	 * Enable varargs
	 */
	public final void enableVarargs() {
		if ( argumentNumberRange == null ) {
			this.varargs = true;
		}
	}
	
	/**
	 * Specify the number of parameters processed
	 * 
	 * @param number the number of parameters
	 */
	public final void setArgumentNumber( int number ) {
		if ( argumentNumberRange == null ) {
			this.varargs = false;
			this.argumentNumberRange = Range.is( number );
		}
	}
	
	/**
	 * Specify the maximum and minimum number of supported parameters
	 * 
	 * @param minNumber the minimum number supported
	 * @param maxNumber the maximum number supported
	 */
	public final void setArgumentNumbers( int minNumber, int maxNumber ) {
		if ( argumentNumberRange == null ) {
			this.varargs = false;
			Assert.isTrue( maxNumber > minNumber, "'maxNumber' cannot be less than 'minNumber'" );
			this.argumentNumberRange = Range.between( minNumber, maxNumber );
		}
	}
	
	/**
	 * Generate a validation rule template for printing text when an exception occurs.
	 * 
	 * @param fragmentName the fragment name for validation rules
	 * @return the validation rule template text
	 */
	private String getTemplate( String fragmentName ) {
		if ( varargs ) {
			return fragmentName + "(...)";
		}
		if ( argumentNumberRange == null ) {
			return fragmentName;
		}
		StringBuilder appender = new StringBuilder( fragmentName );
		Integer minimum = argumentNumberRange.getMinimum();
		Integer maximum = argumentNumberRange.getMaximum();
		if ( minimum == maximum && minimum == 1 ) {
			appender.append( "(?)" );
		} else {
			appender.append( "(" );
			for ( int i = minimum; i < maximum; i ++ ) {
				if ( i > 0 ) {
					appender.append( "," );
				}
				appender.append( "?" );
			}
			appender.append( ")" );
		}
		return appender.toString();
	}
	
	/**
	 * Do some basic parameter limit validation
	 * 
	 * @param fragment the fragment of current validation rule
	 */
	private void doArgumentsCheck( Fragment fragment ) {
		if ( varargs ) {
			return;
		}
		final Integer argNum = fragment.argumentNumber();
		
		// No parameters required
		if ( argumentNumberRange == null ) {
			if ( argNum != 0 ) {
				String template = getTemplate( fragment.getName() );
				throw new ValidatorException( "Handler \"{0}\" cannot accept any parameters", template );
			}
			return;
		}
		
		// [x..y], Specifying acceptable parameter ranges
		if ( argumentNumberRange.contains( argNum ) ) {
			return;
		}
		Integer minimum = argumentNumberRange.getMinimum();
		Integer maximum = argumentNumberRange.getMaximum();
		
		// Out of bounds detection
		if ( !argumentNumberRange.isBefore( argNum ) ) {
			String template = getTemplate( fragment.getName() );
			throw new ValidatorException( "Handler \"{0}\" requires at least {1} parameter{2}", template, minimum, minimum > 1 ? "s" : "" );
		}
		if ( !argumentNumberRange.isAfter( argNum ) ) {
			String template = getTemplate( fragment.getName() );
			String suffix = maximum == 1 ? "at most 1 parameter" : "up to " + maximum + " parameters";
			throw new ValidatorException( "Handler \"{0}\" can only process {1}", template, suffix );
		}
	}

}

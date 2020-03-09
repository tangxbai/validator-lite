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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.ArrayUtils;

import com.viiyue.plugins.validator.constraints.Min;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.utils.TextUtil;

/**
 * Validates the annotated objects should be at least the specified length.
 * 
 * <p>Supported types are:
 * <ul>
 *     <li>{@link CharSequence} implementation class</li>
 *     <li>{@link Number} implementation class</li>
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see Min
 */
public final class MinHandler extends BaseHandler {

	public MinHandler() {
		super( "min" );
		super.setArgumentNumber( 1 );
	}

	@Override
	public Class<?> [] supports() {
		return ArrayUtils.toArray( Number.class, CharSequence.class );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		if ( value instanceof CharSequence ) {
			Integer minValue = fragment.getArgument( 0, Integer.class );
			Integer target = TextUtil.length( value.toString() );
			return target >= minValue;
		}
		if ( value instanceof Double || value instanceof Float ) {
			Double minValue = fragment.getArgument( 0, Double.class );
			Double target = ( ( Number ) value ).doubleValue();
			return target >= minValue;
		}
		if ( value instanceof BigDecimal ) {
			BigDecimal decimal = ( BigDecimal ) value;
			BigDecimal minValue = BigDecimal.valueOf( fragment.getArgument( 0, Long.class ) );
			return decimal.compareTo( minValue ) >= 0;
		}
		if ( value instanceof BigInteger ) {
			BigInteger bigInt = ( BigInteger ) value;
			BigInteger minValue = BigInteger.valueOf( fragment.getArgument( 0, Long.class ) );
			return bigInt.compareTo( minValue ) >= 0;
		}
		if ( value instanceof Number ) {
			Long minValue = fragment.getArgument( 0, Long.class );
			Long target = ( ( Number ) value ).longValue();
			return target >= minValue;
		}
		return false;
	}
	
}
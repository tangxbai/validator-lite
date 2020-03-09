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
import org.apache.commons.lang3.Range;

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.utils.TextUtil;

/**
 * Check that the character sequence length is between min and max.
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
 * @see com.viiyue.plugins.validator.constraints.Range
 */
public final class RangeHandler extends BaseHandler {
	
	public RangeHandler() {
		super( "range" );
		super.setArgumentNumber( 2 );
	}

	@Override
	public Class<?> [] supports() {
		return ArrayUtils.toArray( Number.class, CharSequence.class );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		if ( value instanceof CharSequence ) {
			Integer target = TextUtil.length( value.toString() );
			Integer min = fragment.getArgument( 0, Integer.class );
			Integer max = fragment.getArgument( 1, Integer.class );
			return Range.between( min, max ).contains( target );
		}
		if ( value instanceof Double || value instanceof Float ) {
			Double target = ( ( Number ) value ).doubleValue();
			Double min = fragment.getArgument( 0, Double.class );
			Double max = fragment.getArgument( 1, Double.class );
			return Range.between( min, max ).contains( target );
		}
		if ( value instanceof BigDecimal ) {
			BigDecimal target = ( BigDecimal ) value;
			BigDecimal min = BigDecimal.valueOf( fragment.getArgument( 0, Long.class ) );
			BigDecimal max = BigDecimal.valueOf( fragment.getArgument( 1, Long.class ) );
			return Range.between( min, max ).contains( target );
		}
		if ( value instanceof BigInteger ) {
			BigInteger target = ( BigInteger ) value;
			BigInteger min = BigInteger.valueOf( fragment.getArgument( 0, Long.class ) );
			BigInteger max = BigInteger.valueOf( fragment.getArgument( 1, Long.class ) );
			return Range.between( min, max ).contains( target );
		}
		if ( value instanceof Number ) {
			Long target = ( ( Number ) value ).longValue();
			Long min = fragment.getArgument( 0, Long.class );
			Long max = fragment.getArgument( 1, Long.class );
			return Range.between( min, max ).contains( target );
		}
		return false;
	}
	
}

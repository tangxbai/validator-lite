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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.viiyue.plugins.validator.constraints.Length;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.utils.TextUtil;

/**
 * Validtes the length of the annotated object must be the specified length.
 * 
 * <p>Supported types are:
 * <ul>
 *     <li>{@link CharSequence} implementation class - Calculate the character length.</li>
 *     <li>{@link Collection} implementation class - Count the number of elements in a collection.</li>
 *     <li>{@link Map} implementation class - Count the number of elements in the map.</li>
 *     <li>{@link Object Object[]} - Count the number of elements in an array.</li>
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see Length
 */
public final class LengthHandler extends BaseHandler {
	
	public LengthHandler() {
		super( "length" );
		super.setArgumentNumber( 1 );
	}
	
	@Override
	public Class<?> [] supports() {
		return ArrayUtils.toArray( CharSequence.class, Object[].class, Collection.class, Map.class );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		Integer length = null;
		Integer target = fragment.getArgument( 0, Integer.class );
		
		if ( value instanceof CharSequence ) {
			length = TextUtil.length( value.toString() );
		} else if ( value instanceof Collection ) {
			length = ( ( Collection<?> ) value ).size();
		} else if ( value instanceof Map ) {
			length = ( ( Map<?, ?> ) value ).size();
		} else if ( value.getClass().isArray() ) {
			length = Array.getLength( value );
		}
		return Objects.equals( length, target );
	}
	
}

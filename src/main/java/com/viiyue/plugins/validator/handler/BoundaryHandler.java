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

import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.constraints.EndsWith;
import com.viiyue.plugins.validator.constraints.StartsWith;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;
import com.viiyue.plugins.validator.utils.TextUtil;

/**
 * Boundary match validation handler
 * 
 * <ul>
 *     <li>Annotation - &#64;StartsWith(elements = {?, ?, ?, ...})</li>
 *     <li>Rule template - "prefixs(?, ?, ?, ...)"</li>
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see StartsWith
 * @see EndsWith
 */
public final class BoundaryHandler extends BaseHandler {
	
	public BoundaryHandler() {
		super( "prefixs", "suffixs" );
		super.enableVarargs();
	}
	
	@Override
	public boolean support( Class<?> valueType ) {
		return ClassUtils.isAssignable( valueType, CharSequence.class );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		String stringValue = value.toString();
		if ( StringUtils.isEmpty( stringValue ) ) {
			return false;
		}
		Object [] arguments = fragment.getArguments();
		
		if ( Objects.equals( fragment.getName(), "prefixs" ) ) {
			if ( TextUtil.startsWith( stringValue, arguments ) ) {
				return true;
			}
		}
		if ( Objects.equals( fragment.getName(), "suffixs" ) ) {
			if ( TextUtil.endsWith( stringValue, arguments ) ) {
				return true;
			}
		}
		// Solve the value of the expression {elements}
		context.set( "elements", StringUtils.join( arguments, ", " ) );
		return false;
	}
	
}

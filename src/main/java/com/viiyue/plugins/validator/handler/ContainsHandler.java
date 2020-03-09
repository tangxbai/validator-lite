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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.constraints.Contains;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * Validates the annotated object should be in the specified element array.
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see Contains
 */
public final class ContainsHandler extends BaseHandler {
	
	public ContainsHandler() {
		super( "contains" );
		super.enableVarargs();
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		Object [] arguments = fragment.getArguments();
		if ( ArrayUtils.contains( arguments, value ) ) {
			return true;
		}
		
		// Solve the value of the expression '{elements}'
		context.set( "elements", StringUtils.join( arguments, ", " ) );
		return false;
	}
	
}

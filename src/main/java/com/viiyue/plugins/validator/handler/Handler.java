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

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * Abstract interface for data validation. Different validation rules are
 * handled by specific implementation classes.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public interface Handler {

	/**
	 * Bound validation rule name
	 * 
	 * @return the name of the validation rule
	 */
	String name();
	
	/**
	 * Does the handler support this data type?
	 * 
	 * @param valueType the target value type
	 * @return true for support, otherwise it is not supported.
	 */
	boolean support( Class<?> valueType );
	
	/**
	 * Handle specific validation logic
	 * 
	 * @param value the target validation value
	 * @param fragment the fragment of current validation rule
	 * @param context the validation rule context object
	 * @return true if the validation is passed, otherwise the validation is rejected.
	 */
	boolean doHandle( Object value, Fragment fragment, Context context );
	
}

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
package com.viiyue.plugins.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Conditions under which rule validation takes effect
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Target({ FIELD, METHOD })
@Retention( RUNTIME )
@Documented
public @interface When {
	
	/**
	 * @return an array of fields that need to be validated
	 */
	String[] fields();
	
	/**
	 * @return to the scene in effect
	 */
	Result is();
	
	/**
	 * Scenario in which the test takes effect
	 * 
	 * @author tangxbai
	 * @since 1.0
	 */
	public static enum Result {
		PASSED, REJECTED
	}
	
}

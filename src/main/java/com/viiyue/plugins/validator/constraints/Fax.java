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
package com.viiyue.plugins.validator.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.viiyue.plugins.validator.annotation.Mapping;
import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.provider.DefaultProvider;

/**
 * Validates the annotated string should be a fax address.
 * 
 * <p>Regular expression: "^(0\d{2}-\d{8}(-\d{1,4})?)|(0\d{3}-\d{7,8}(-\d{1,4})?)$"</p>
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Target({ PARAMETER, FIELD, METHOD })
@Retention( RUNTIME )
@Documented
@Mapping( provider = DefaultProvider.class )
public @interface Fax {
	
	Class<?>[] groups() default {};
	String message() default Constants.EMPTY_STRING;
	
}

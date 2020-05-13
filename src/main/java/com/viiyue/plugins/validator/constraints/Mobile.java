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
 * Validates the annotated string is a valid mobile phone number, the number range must be
 * combination of 11 digits beginning with {@code 13}, {@code 14}, {@code 15}, {@code 16}, {@code 17}, {@code 18}.
 * 
 * <p>Regular expression: "^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2,5,6,7]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$"</p>
 * 
 * @author tangxbai
 * @since 1.0.4
 */
@Target({ PARAMETER, FIELD, METHOD })
@Retention( RUNTIME )
@Documented
@Mapping( provider = DefaultProvider.class )
public @interface Mobile {
	
	Class<?>[] groups() default {};
	String message() default Constants.EMPTY_STRING;
	
}

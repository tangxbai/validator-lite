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
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.viiyue.plugins.validator.common.Constants;

/**
 * <p>Used to set the display text of the field
 * <p>The following three configuration methods are supported:
 * 
 * <ul>
 * <li>&#64;Label - You just need to declare, the program will use
 * {@code field.getName()} (e.g. "com.a.b.c.Bean.field") to find the resource file and get the text automatically.
 * <li>&#64;Label("Label display text") - Set display text directly.
 * <li>&#64;Label("{message.key}") - Reference the message text in the ResourceBundle.
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 */
@Target({ FIELD, METHOD, PARAMETER })
@Retention( RUNTIME )
@Documented
public @interface Label {
	String value() default Constants.EMPTY_STRING;
}
 
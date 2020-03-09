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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.validator.constraints.Required;

/**
 * Marks a property, method parameter or method return type for validation cascading.
 * 
 * <p>
 * Constraints defined on the object and its properties are be validated when the
 * property, method parameter or method return type is validated.
 * 
 * <p>This behavior is applied recursively.
 * 
 * <p>
 * The annotation can be used with {@link Required @Required}, {@link Label @Label}, {@link When @When}.
 *
 * <ul>
 * <li>{@link Required @Required} - Used to mark whether it is an internal bean validation object
 * <li>{@link Label @Label} - Used to set the display text of the field
 * <li>{@link When @When} - Used to mark conditions that trigger validation
 * </ul>
 *
 * <p>Code example: 
 * <pre>public class Bean {
 *     
 *     &#64;Label("Base name")
 *     &#64;Required
 *     &#64;NotBlank
 *     &#64;Range(10,20)
 *     &#64;Pattern(regepx = "^(\\w)+$")
 *     private String name;
 *     
 *     &#64;Valid
 *     &#64;Label("My bean")
 *     &#64;Required &#47;&#47; This bean object cannot be empty
 *     private AnotherBean anotherBean;
 *      
 * }
 * 
 * public class AnotherBean {
 * 
 *     &#64;Label("My name")
 *     &#64;Required
 *     &#64;NotBlank
 *     private String myName;
 *     
 * }</pre>
 *
 * @author tangxbai
 * @since 1.0.0
 */
@Target({ FIELD, METHOD })
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Valid {}

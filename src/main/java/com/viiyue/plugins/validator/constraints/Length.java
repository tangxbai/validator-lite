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
import java.util.Collection;
import java.util.Map;

import com.viiyue.plugins.validator.annotation.Mapping;
import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.constraints.Length.LengthProvider;
import com.viiyue.plugins.validator.handler.LengthHandler;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.provider.AnnotationProvider;

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
 * @see LengthHandler
 */
@Target({ PARAMETER, FIELD, METHOD })
@Retention( RUNTIME )
@Documented
@Mapping( usage = "length(?)", provider = LengthProvider.class )
public @interface Length {
	
	int value();
	
	Class<?>[] groups() default {};
	String message() default Constants.EMPTY_STRING;
	
	class LengthProvider implements AnnotationProvider<Length> {
		@Override
		public Fragment create( Length defined, String defaultName ) {
			return Fragment.of( defaultName )
				.groups( defined.groups() )
				.message( defined.message() )
				.arguments( defined.value() );
		}
	}
	
}

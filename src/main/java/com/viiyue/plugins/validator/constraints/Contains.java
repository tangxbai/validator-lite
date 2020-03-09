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

import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.annotation.Mapping;
import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.constraints.Contains.ContainsProvider;
import com.viiyue.plugins.validator.handler.ContainsHandler;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.provider.AnnotationProvider;
import com.viiyue.plugins.validator.scripting.ExpressionResolver;

/**
 * Validates the annotated object should be in the specified element array.
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see ContainsHandler
 */
@Target({ PARAMETER, FIELD, METHOD })
@Retention( RUNTIME )
@Documented
@Mapping( usage = "contains(?, ?, ?, ...)", provider = ContainsProvider.class )
public @interface Contains {
	
	String [] elements();
	
	Class<?>[] groups() default {};
	String message() default Constants.EMPTY_STRING;
	
	class ContainsProvider implements AnnotationProvider<Contains> {
		@Override
		public Fragment create( Contains defined, String defaultName ) {
			String element = StringUtils.join( defined.elements(), Constants.SEPARATOR );
			Object [] elements = ExpressionResolver.resolveArguments( element );
			return Fragment.of( defaultName ).groups( defined.groups() ).message( defined.message() ).arguments( elements );
		}
	}
	
}

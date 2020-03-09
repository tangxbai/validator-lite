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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.provider.AnnotationProvider;

/**
 * Map annotation constraints to validation fragments to collect validation
 * rules provided by annotation constraints.
 *
 * @author tangxbai
 * @since 1.0.0
 */
@Target( ElementType.ANNOTATION_TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Mapping {
	
	/**
	 * Provide the name of the generated validation fragment
	 * 
	 * @return fragment name
	 */
	String name() default Constants.EMPTY_STRING;
	
	/**
	 * @return to basic usage
	 */
	String usage() default Constants.EMPTY_STRING;
	
	/**
	 * The type of provider that generated the validation fragment
	 * 
	 * @return the fragment provider type
	 */
	Class<? extends AnnotationProvider<? extends Annotation>> provider();
	
}

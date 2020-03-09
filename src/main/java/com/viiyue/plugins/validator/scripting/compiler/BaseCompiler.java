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
package com.viiyue.plugins.validator.scripting.compiler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.viiyue.plugins.validator.scripting.CacheableContent;

/**
 * Abstract compiler, providing a common way to get annotations.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
abstract class BaseCompiler {

	private static final CacheableContent<Field, Method> methods = new CacheableContent<Field, Method>( 256 );
	private static final CacheableContent.Provider<Field, Method> methodProvider = new CacheableContent.Provider<Field, Method>() {
		@Override
		public Method create( Field field ) {
			Method readMethod = null;
			try {
				PropertyDescriptor descriptor = new PropertyDescriptor( field.getName(), field.getDeclaringClass() );
				if ( descriptor != null ) {
					readMethod = descriptor.getReadMethod();
				}
			} catch ( IntrospectionException e ) {
			}
			return readMethod;
		}
	};
	
	/**
	 * Provide a unified way to obtain annotations. First, the annotation object
	 * will be obtained directly from the element itself. If the return result
	 * is null, try to use the Getter method of the property to obtain the
	 * annotation. If it is null again, the element is considered to have no
	 * relevant annotation.
	 * 
	 * @param element the annotated element
	 * @param annotationType the annotated element class type
	 * @return the obtained annotation object
	 */
	public <T extends Annotation> T getAnnotation( AnnotatedElement element, Class<T> annotationType ) {
		T annotation = element.getAnnotation( annotationType );
		if ( annotation == null && element instanceof Field ) {
			Method readMethod = methods.getOrPut( ( Field ) element, methodProvider );
			if ( readMethod != null ) {
				annotation = readMethod.getAnnotation( annotationType );
			}
		}
		return annotation;
	}
	
}

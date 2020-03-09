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
package com.viiyue.plugins.validator.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.viiyue.plugins.validator.exception.ReflectionException;

/**
 * Utility methods for using Java Reflection APIs to facilitate generic property
 * getter and setter operations on Java objects.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class PropertyUtil {

	private PropertyUtil() {}

	public static PropertyDescriptor getDescriptor( Class<?> type, String fieldName ) {
		try {
			return new PropertyDescriptor( fieldName, type );
		} catch ( IntrospectionException e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}

	public static PropertyDescriptor [] getPropertyDescriptors( Class<?> type ) {
		try {
			return Introspector.getBeanInfo( type ).getPropertyDescriptors();
		} catch ( IntrospectionException e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}

}

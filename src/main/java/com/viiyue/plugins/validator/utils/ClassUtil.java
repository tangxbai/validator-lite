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

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

import com.viiyue.plugins.validator.exception.ReflectionException;

/**
 * Entity class processing tool class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class ClassUtil {

	private ClassUtil() {}

	public static Class<?> forName( String className ) {
		try {
			return ClassUtils.getClass( getDefaultClassLoader(), className );
		} catch ( ClassNotFoundException e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}
	
	public static Set<Class<?>> getAllSuperclasses( Class<?> clazz ) {
		final Set<Class<?>> visited = new LinkedHashSet<Class<?>>( 16 );
		for ( Class<?> type = clazz; type != null && type != Object.class; type = type.getSuperclass() ) {
			visited.add( type );
		}
		return visited;
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader loader = null;
		try {
			loader = Thread.currentThread().getContextClassLoader();
		} catch ( Throwable ex ) {
		}
		if ( loader == null ) {
			loader = ClassUtil.class.getClassLoader();
		}
		if ( loader == null ) {
			try {
				loader = ClassLoader.getSystemClassLoader();
			} catch ( Throwable ex ) {
			}
		}
		return loader;
	}

}

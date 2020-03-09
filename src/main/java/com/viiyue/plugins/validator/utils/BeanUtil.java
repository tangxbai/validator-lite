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

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import com.viiyue.plugins.validator.exception.ReflectionException;

/**
 * Javabean operation tool class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class BeanUtil {

	private BeanUtil() {}

	public static <T> T newInstance( Class<T> type, Object ... arguments ) {
		try {
			if ( ArrayUtils.isEmpty( arguments ) ) {
				return type.newInstance();
			}
			Constructor<T> constructor = type.getConstructor( ClassUtils.toClass( arguments ) );
			return constructor.newInstance( arguments );
		} catch ( Exception e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}
	
}

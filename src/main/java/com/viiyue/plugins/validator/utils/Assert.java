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

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import com.viiyue.plugins.validator.exception.ArgumentException;

/**
 * Argument assertion tool class
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Assert {

	public static void isTrue( boolean expression, String message, Object ... arguments ) {
		if ( !expression ) {
			throwing( message, arguments );
		}
	}

	public static void isFalse( boolean expression, String message, Object ... arguments ) {
		if ( expression ) {
			throwing( message, arguments );
		}
	}
	
	public static void isAssignable( Class<?> clazz, Class<?> target, String message, Object ... arguments ) {
		if ( !ClassUtils.isAssignable( clazz, target, true ) ) {
			throwing( message, arguments );
		}
	}

	public static void isNull( Object object, String message, Object ... arguments ) {
		if ( object != null ) {
			throwing( message, arguments );
		}
	}

	public static void notNull( Object object, String message, Object ... arguments ) {
		if ( object == null ) {
			throwing( message, arguments );
		}
	}

	public static void isEmpty( Collection<?> collection, String message, Object ... arguments ) {
		if ( !CollectionUtils.isEmpty( collection ) ) {
			throwing( message, arguments );
		}
	}

	public static void notEmpty( Collection<?> collection, String message, Object ... arguments ) {
		if ( CollectionUtils.isEmpty( collection ) ) {
			throwing( message, arguments );
		}
	}

	public static void notEmpty( Map<?, ?> map, String message, Object ... arguments ) {
		if ( MapUtils.isEmpty( map ) ) {
			throwing( message, arguments );
		}
	}

	public static void notEmpty( Object [] array, String message, Object ... arguments ) {
		if ( ArrayUtils.isEmpty( array ) ) {
			throwing( message, arguments );
		}
	}

	public static void throwing( String message, Object ... arguments ) {
		throw new ArgumentException( message, arguments );
	}
	
}

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

import java.util.Objects;

/**
 * Simple array manipulation tool class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class ArrayUtil {

	private ArrayUtil() {}

	/**
	 * Determines whether the given array is empty, returning {@code true} if
	 * the object is null or the length of the array is 0.
	 * 
	 * @param array target object array, may be {@code null}.
	 * @return {@code true} if the target array is null or contains no elements
	 */
	public static boolean isEmpty( Object [] array ) {
		return array == null || array.length == 0;
	}

	/**
	 * Determines whether the given array is not empty, returning {@code true}
	 * if the object is not null or the array contains any elements.
	 * 
	 * @param array target object array, may be {@code null}.
	 * @return {@code true} if the target array contains any elements
	 */
	public static boolean isNotEmpty( Object [] array ) {
		return !isEmpty( array );
	}
	
	/**
	 * <p>
	 * Compares given <code>target</code> to a Objects vararg of {@code elements}, or
	 * returning {@code true} if the <code>target</code> is equal to any of the <code>elements</code>.</p>
	 * 
     * <pre>
     * ArrayUtil.equalsAny(null, (Object[]) null) = false
     * ArrayUtil.equalsAny(null, null, null)    = true
     * ArrayUtil.equalsAny(null, "abc", "def")  = false
     * ArrayUtil.equalsAny("abc", null, "def")  = false
     * ArrayUtil.equalsAny("abc", "abc", "def") = true
     * ArrayUtil.equalsAny("abc", "ABC", "DEF") = false
     * </pre>
     * 
	 * @param target to compare, may be {@code null}.
	 * @param elements a vararg of objects, may be {@code null}.
	 * @return {@code true} if the target is equal to any other element of <code>elements</code>;
     * {@code false} if <code>elements</code> is null or contains no matches.
	 */
	public static boolean equalsAny( Object target, Object ... elements ) {
		if ( isNotEmpty( elements ) ) {
			for ( Object element : elements ) {
				if ( Objects.equals( target, element ) ) {
					return true;
				}
			}
		}
		return false;
	}

}

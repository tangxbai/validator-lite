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

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.viiyue.plugins.validator.exception.ReflectionException;

/**
 * Simple singleton tool class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class SingletonUtil {
	
	private static final ConcurrentMap<Class<?>, Future<Object>> singletons = new ConcurrentHashMap<Class<?>, Future<Object>>( 1 << 6 );
	
	public static <V> V getBean( final Class<V> beanType, final Object ... arguments ) {
		Future<Object> future = singletons.get( beanType );
		if ( future == null ) {
			FutureTask<Object> task = new FutureTask<Object>( new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return BeanUtil.newInstance( beanType, arguments );
				}
			});
			future = singletons.putIfAbsent( beanType, task );
			if ( future == null ) {
				future = task;
				task.run();
			}
		}
		return ( V ) getTaskResult( beanType, future );
	}
	
	private static <V> V getTaskResult( final Class<?> beanType, final Future<V> task ) {
		try {
			return task.get();
		} catch ( Exception e ) {
			singletons.remove( beanType );
			throw new ReflectionException( e.getMessage(), e );
		}
	}
	
}

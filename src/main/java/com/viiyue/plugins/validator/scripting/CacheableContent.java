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
package com.viiyue.plugins.validator.scripting;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.viiyue.plugins.validator.exception.ExpressionException;

/**
 * Supports cacheable content management objects. 
 * <p>This class is thread-safe and supports use in multi-threaded mode.
 * 
 * <p>#ThreadSafe#</p>
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see FutureTask
 * @see ConcurrentMap
 */
public class CacheableContent<K, V> {

	private final ConcurrentMap<K, Future<V>> caches;
	
	public CacheableContent() {
		this( 16 );
	}
	
	public CacheableContent( final int initialCapacity ) {
		this.caches = new ConcurrentHashMap<K, Future<V>>( initialCapacity );
	}
	
	public V getOrPut( final K key, final Provider<K, V> provider ) {
		Future<V> future = this.caches.get( key );
		if ( future != null ) {
			return getTaskResult( key, future );
		}
		FutureTask<V> task = new FutureTask<V>( new Callable<V>() {
			@Override
			public V call() throws Exception {
				return provider.create( key );
			}
		});
		future = this.caches.putIfAbsent( key, task );
		if ( future == null ) {
			future = task;
			task.run();
		}
		return getTaskResult( key, future );
	}
	
	private V getTaskResult( final K key, final Future<V> task ) {
		try {
			return task.get();
		} catch ( Exception e ) {
			this.caches.remove( key );
			throw new ExpressionException( e.getMessage(), e );
		}
	}
	
	public static interface Provider<K, V> {
		V create( K key );
	}
	
}

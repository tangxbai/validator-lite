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
package com.viiyue.plugins.validator.metadata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.exception.TypeMismatchException;
import com.viiyue.plugins.validator.utils.ArrayUtil;
import com.viiyue.plugins.validator.utils.Assert;

/**
 * Validation rule fragmentation.
 * 
 * <p>
 * In the {@code validator-lite} framework,
 * validation rules are divided into specific elements. Each element contains
 * multiple fragments of validation rules. The fragment contains some basic
 * information required for validation.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Fragment {

	private String name;
	private Set<String> groups;
	private Object [] arguments;
	private String message;
	private String template;

	public Fragment() {
	}
	
	public Fragment( String name ) {
		this.name( name );
	}
	
	// Getter
	
	public String getName() {
		return name;
	}

	public Set<String> getGroups() {
		return groups == null ? null : Collections.unmodifiableSet( groups );
	}

	public Object [] getArguments() {
		return arguments;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTemplate() {
		return template;
	}
	
	// Setter
	
	public Fragment name( String name ) {
		this.name = name;
		this.template = name;
		return this;
	}
	
	public Fragment groups( String[] groups ) {
		if ( ArrayUtil.isNotEmpty( groups ) ) {
			this.groups = new HashSet<String>( groups.length );
			for ( String group : groups ) {
				if ( StringUtils.isNotEmpty( group ) ) {
					this.groups.add( group ); // Short name
				}
			}
		}
		return this;
	}

	public Fragment groups( Class<?>[] groups ) {
		if ( ArrayUtil.isNotEmpty( groups ) ) {
			this.groups = new HashSet<String>( groups.length );
			for ( Class<?> group : groups ) {
				this.groups.add( group.getName() ); // Class name
			}
		}
		return this;
	}
	
	public Fragment addGroup( String group ) {
		Assert.notNull( group, "'group' is required" );
		if ( groups == null ) {
			this.groups = new HashSet<String>( 4 );
		}
		this.groups.add( group );
		return this;
	}
	
	public Fragment addGroup( Class<?> group ) {
		Assert.notNull( group, "'group' is required" );
		return addGroup( group.getName() );
	}
	
	public Fragment arguments( Object ... arguments ) {
		this.arguments = arguments;
		this.template = this.name + ( ArrayUtil.isEmpty( arguments ) ? "" : "(...)" );
		return this;
	}

	public Fragment message( String message ) {
		this.message = message;
		return this;
	}
	
	// Helper method
	
	public boolean hasGroups() {
		return groups != null;
	}
	
	public boolean isInGroup( Class<?> group ) {
		if ( group == null ) {
			return true;
		}
		if ( CollectionUtils.isEmpty( groups ) ) {
			return Objects.equals( Constants.DEFAULT_GROUP, group );
		}
		return ( groups.contains( group.getName() ) || groups.contains( group.getSimpleName() ) );
	}

	public int argumentNumber() {
		return hasArguments() ? arguments.length : 0;
	}

	public boolean isNoArguments() {
		return ArrayUtil.isEmpty( arguments );
	}

	public boolean hasArguments() {
		return ArrayUtil.isNotEmpty( arguments );
	}

	public Object getArgument( int index ) {
		if ( hasArguments() && ( index >= 0 && index < arguments.length ) ) {
			return arguments[ index ];
		}
		return null;
	}

	public <T> T getArgument( int index, Class<T> type ) {
		Object argument = getArgument( index );
		if ( argument == null ) {
			return null;
		}
		
		Class<?> argType = argument.getClass();
		if ( Objects.equals( argType, type ) ) {
			return ( T ) argument;
		}
		
		// Converter conversion
		Converter converter = ConvertUtils.lookup( argType );
		if ( converter != null ) {
			try {
				return converter.convert( type, argument );
			} catch ( ConversionException e ) {
				throw new TypeMismatchException( type, "Parameter value <{0}> does not match type <{1}>", argument, type.getName() );
			}
		}
		
		// Converting data types directly
		try {
			return ( T ) argument;
		} catch ( Exception e ) {
			throw new TypeMismatchException( type, "Parameter value <{0}> does not match type <{1}>", argument, type.getName() );
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[ " + template + " ]" + "@" + Integer.toHexString( hashCode() );
	}
	
	public static final Fragment of( String name ) {
		return new Fragment( name );
	}

}

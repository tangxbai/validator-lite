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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.jexl3.JexlContext;

import com.viiyue.plugins.validator.ValidatorFactory;
import com.viiyue.plugins.validator.metadata.Element;

/**
 * Context object for validating fragment rules
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Context implements JexlContext {

	private final Locale locale;
	private final Element element;
	private final Object instance;
	private final ValidatorFactory factory;
	private final Map<String, Object> variables;
	
	private String[] messageKeys;

	public Context( ValidatorFactory factory, Element element, Object instance, Locale locale ) {
		this.factory = factory;
		this.element = element;
		this.instance = instance;
		this.locale = locale;
		this.variables = new HashMap<String, Object>( 8 );
	}

	@Override
	public boolean has( String name ) {
		return variables.containsKey( name );
	}

	@Override
	public Object get( String name ) {
		return variables.get( name );
	}

	@Override
	public void set( String name, Object value ) {
		variables.put( name, value );
	}

	public void clear() {
		variables.clear();
	}

	public Element getElement() {
		return element;
	}

	public Object getInstance() {
		return instance;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public ValidatorFactory getFactory() {
		return factory;
	}

	public String[] getMessageKeys() {
		return messageKeys;
	}

	public void setMessageKeys( String ... messageKeys ) {
		this.messageKeys = messageKeys;
	}
	
}

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
package com.viiyue.plugins.validator.scripting.message;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viiyue.plugins.validator.Validator;
import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;
import com.viiyue.plugins.validator.scripting.configuration.MessageResource;

/**
 * Internationalized message abstract interface for extending text characters to be displayed
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see DefaultMessageResolver
 */
public interface MessageResolver {
	
	static final Logger LOG = LoggerFactory.getLogger( Validator.class );
	
	void setConfiguration( ContextConfigurion configuration );
	ContextConfigurion getConfiguration();
	
	void setDefaultLocale( Locale defaultLocale );
	Locale getDefaultLocale();

	void addResourceBundle( String resourceName, String ... preloadings );
	void addResourceBundles( List<MessageResource> resources );
	Set<ResourceBundle> getResourceBundles();

	String getMessageKey( String key );

	String resolve( String key );
	String resolve( String key, String defaultValue );
	
	String resolve( String key, Locale locale );
	String resolve( String key, Locale locale, String defaultValue );
	
}

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;
import com.viiyue.plugins.validator.scripting.configuration.MessageResource;
import com.viiyue.plugins.validator.utils.ArrayUtil;
import com.viiyue.plugins.validator.utils.Assert;
import com.viiyue.plugins.validator.utils.ClassUtil;

/**
 * <p>
 * Default internationalized message parser implementation class, allowing users
 * to override the default message text.
 * 
 * <p>
 * The default internationalization resource file is at: "{@code com/viiyue/plugins/validator/resources/Message}".
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public class DefaultMessageResolver implements MessageResolver {
	
	private final String keyPrefix;
	private final String internalResourceName;
	private final List<String> externalResourceNames;
	private volatile Locale defaultLocale;
	private volatile ContextConfigurion configuration;

	private final ConcurrentMap<String, ResourceBundle> externalBundles = new ConcurrentHashMap<String, ResourceBundle>( 32 );
	private final ConcurrentMap<String, ResourceBundle> internalBundles = new ConcurrentHashMap<String, ResourceBundle>( 32 );
	
	public DefaultMessageResolver( String internalResourceName, String keyPrefix, String ... defaultLanguages ) {
		Assert.notNull( internalResourceName, "'internalResourceName' parameter is required" );
		this.internalResourceName = internalResourceName;
		this.keyPrefix = keyPrefix == null ? "" : keyPrefix + ".";
		this.externalResourceNames = new ArrayList<String>( 10 );
		this.setDefaultLocale( Locale.getDefault() );
		this.addResourceBundle( false, internalResourceName, defaultLanguages );
	}
	
	@Override
	public void setConfiguration( ContextConfigurion configuration ) {
		this.configuration = configuration;
	}
	
	@Override
	public ContextConfigurion getConfiguration() {
		return configuration;
	}

	@Override
	public void setDefaultLocale( Locale defaultLocale ) {
		this.defaultLocale = defaultLocale;
	}
	
	@Override
	public Locale getDefaultLocale() {
		if ( defaultLocale == null ) {
			this.defaultLocale = Locale.getDefault();
		}
		return defaultLocale;
	}

	@Override
	public void addResourceBundle( String resourceName, String ... preloadings ) {
		addResourceBundle( true, resourceName, preloadings );
	}

	@Override
	public void addResourceBundles( List<MessageResource> resources ) {
		if ( CollectionUtils.isNotEmpty( resources ) ) {
			for ( MessageResource resource : resources ) {
				addResourceBundle( resource.getBaseName(), resource.getPreloadings() );
			}
		}
	}
	
	@Override
	public Set<ResourceBundle> getResourceBundles() {
		Collection<ResourceBundle> internalBundles = this.internalBundles.values();
		Collection<ResourceBundle> externalBundles = this.externalBundles.values();
		Set<ResourceBundle> merged = new HashSet<ResourceBundle>( internalBundles.size() + externalBundles.size() );
		merged.addAll( internalBundles );
		merged.addAll( externalBundles );
		return merged;
	}

	@Override
	public String getMessageKey( String key ) {
		Assert.notNull( key, "Resource key cannot be null" );
		return keyPrefix == null ? key : keyPrefix.concat( key );
	}

	@Override
	public String resolve( String key ) {
		return resolve( key, null, null );
	}

	@Override
	public String resolve( String key, String defaultValue ) {
		return resolve( key, null, defaultValue );
	}

	@Override
	public String resolve( String key, Locale locale ) {
		return resolve( key, locale, null );
	}

	@Override
	public String resolve( String key, Locale locale, String defaultValue ) {
		Assert.notNull( key, "Resource key cannot be null" );
		String resourceText = null;
		ResourceBundle resource = null;
		
		// Get text from a custom resource bundle
		if ( CollectionUtils.isNotEmpty( externalResourceNames ) ) {
			for ( String externalResourceName : externalResourceNames ) {
				try {
					resource = getResourceBundle( externalBundles, locale, externalResourceName );
					resourceText = resource.getString( key );
				} catch ( Exception e ) {
				}
				if ( resourceText != null ) {
					return resourceText;
				}
			}
		}
		
		// Get default text from internal resource bundle
		if ( resourceText == null ) {
			try {
				resource = getResourceBundle( internalBundles, locale, internalResourceName );
				resourceText = resource.getString( key );
			} catch ( MissingResourceException ex ) {
				// LOG.warn( "No resource text found with key \"{}\"", key );
			}
		}
		
		// Finally, if no value is obtained, the default value is used instead.
		return ObjectUtils.defaultIfNull( resourceText, defaultValue );
	}
	
	
	private void addResourceBundle( boolean isExternal, String baseResourceName, String ... languages ) {
		ConcurrentMap<String, ResourceBundle> bundles = isExternal ? externalBundles : internalBundles;
		// Register the default language resource
		initResourceBundle( bundles, baseResourceName, null, null );
		// Register specific language resources
		if ( ArrayUtil.isNotEmpty( languages ) ) {
			for ( String language : languages ) {
				if ( StringUtils.isNotEmpty( language ) ) {
					if ( language.contains( "_" ) ) {
						language = StringUtils.replace( language, "_", "-" );
					}
					initResourceBundle( bundles, baseResourceName, language, Locale.forLanguageTag( language ) );
				}
			}
			if ( isExternal ) {
				this.externalResourceNames.add( baseResourceName );
			}
		}
	}
	
	private void initResourceBundle( ConcurrentMap<String, ResourceBundle> bundles, String baseName, String languageTag, Locale locale ) {
		locale = locale == null ? getDefaultLocale() : locale;
		String cacheKey = getCacheKey( baseName, languageTag );
		ResourceBundle bundle = ResourceBundle.getBundle( baseName, locale, ClassUtil.getDefaultClassLoader() );
		bundles.put( cacheKey, bundle );
	}
	
	private String getCacheKey( String baseName, String languageTag ) {
		String cacheKey = baseName;
		
		// META-INF/resources/Message -> META-INF.resources.Message
		if ( baseName.contains( "/" ) ) {
			cacheKey = StringUtils.replace( cacheKey, "/", "." );
		}
		
		// META-INF.resources.Message -> meta-inf.resources.message
		// com.A.B.C.Message -> com.a.b.c.message
		cacheKey = cacheKey.toLowerCase( Locale.ENGLISH );
		
		// com.a.b.c.message -> com.a.b.c.message.zh-CN
		if ( languageTag != null ) {
			cacheKey = cacheKey.concat( "." ).concat( languageTag );
		}
		return cacheKey;
	}
	
	private ResourceBundle getResourceBundle( Map<String, ResourceBundle> bundles, Locale locale, String baseName ) {
		if ( locale == null ) {
			return bundles.get( getCacheKey( baseName, null ) ); // Bundle key : "a.b.c.e"
		}
		String cacheKey = getCacheKey( baseName, locale.toLanguageTag() ); // Bundle key : "a.b.c.e.zh-CN"
		ResourceBundle resource = bundles.get( cacheKey );
		if ( resource == null ) {
			cacheKey = getCacheKey( baseName, locale.getLanguage() ); // Bundle key : "a.b.c.e.zh"
			resource = bundles.get( cacheKey );
		}
		if ( resource == null ) {
			resource = getResourceBundle( bundles, null, baseName );
		}
		return resource;
	}

}

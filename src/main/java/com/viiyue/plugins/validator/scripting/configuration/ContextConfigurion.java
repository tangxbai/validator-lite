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
package com.viiyue.plugins.validator.scripting.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Validation rule optional configuration object
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public class ContextConfigurion {

	private Locale defaultLanguage;
	private boolean enableStrictMode = true;
	private boolean enableSingleMode = false;
	private boolean enableWarningLog = true;
	private List<MessageResource> resources = new ArrayList<MessageResource>( 4 );

	public boolean isEnableStrictMode() {
		return enableStrictMode;
	}

	public void setEnableStrictMode( boolean enableStrictMode ) {
		this.enableStrictMode = enableStrictMode;
	}

	public boolean isEnableSingleMode() {
		return enableSingleMode;
	}

	public void setEnableSingleMode( boolean enableSingleMode ) {
		this.enableSingleMode = enableSingleMode;
	}
	
	public boolean isEnableWarningLog() {
		return enableWarningLog;
	}

	public void setEnableWarningLog( boolean enableWarningLog ) {
		this.enableWarningLog = enableWarningLog;
	}

	public Locale getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage( String defaultLanguage ) {
		this.defaultLanguage = Locale.forLanguageTag( defaultLanguage );
	}

	public void setDefaultLocale( Locale defaultLanguage ) {
		this.defaultLanguage = defaultLanguage;
	}

	public List<MessageResource> getResources() {
		return resources;
	}

	public void setResources( List<MessageResource> resources ) {
		this.resources = resources;
	}
	
	public void addResource( String baseResourceName, String ... preloadings ) {
		this.resources.add( new MessageResource( baseResourceName, preloadings ) );
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		public final ContextConfigurion config = new ContextConfigurion();
		
		public Builder enableStrictMode() {
			this.config.setEnableStrictMode( true );
			return this;
		}
		
		public Builder disableStrictMode() {
			this.config.setEnableStrictMode( false );
			return this;
		}
		
		public Builder enableSingleMode() {
			this.config.setEnableSingleMode( true );
			return this;
		}
		
		public Builder disableSingleMode() {
			this.config.setEnableSingleMode( false );
			return this;
		}
		
		public Builder enableWarningLog() {
			this.config.setEnableWarningLog( true );
			return this;
		}
		
		public Builder disableWarningLog() {
			this.config.setEnableWarningLog( false );
			return this;
		}
		
		public Builder defaultLanguage( String defaultLanguage ) {
			this.config.setDefaultLanguage( defaultLanguage );
			return this;
		}
		
		public Builder defaultLanguage( Locale defaultLanguage ) {
			this.config.setDefaultLocale( defaultLanguage );
			return this;
		}
		
		public Builder addResource( String baseResourceName, String ... preloadings ) {
			this.config.addResource( baseResourceName, preloadings );
			return this;
		}
		
		public ContextConfigurion build() {
			return this.config;
		}
		
	}

}

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

import org.apache.commons.lang3.StringUtils;

/**
 * Internationalized message resource configuration bean
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class MessageResource {

	private String baseName;
	private String[] preloadings;
	
	public MessageResource() {
	}
	
	public MessageResource( String baseName, String ... preloadings ) {
		this.baseName = baseName;
		this.preloadings = preloadings;
	}

	public String getBaseName() {
		return baseName;
	} 

	public void setBaseName( String baseName ) {
		this.baseName = baseName;
	}

	public String[] getPreloadings() {
		return preloadings;
	}

	public void setPreloadings( String preloadings ) {
		this.preloadings = StringUtils.split( preloadings, ',' );
	}
	
	public void setPreloadings( String ... preloadings ) {
		this.preloadings = preloadings;
	}

}

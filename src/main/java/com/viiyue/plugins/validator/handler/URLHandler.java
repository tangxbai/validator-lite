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
package com.viiyue.plugins.validator.handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * Validation handler for the {@link com.viiyue.plugins.validator.constraints.URL URL} constraint annotation.
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see com.viiyue.plugins.validator.constraints.URL
 */
public final class URLHandler extends BaseHandler {
	
	public URLHandler() {
		super( "url" );
		super.setArgumentNumbers( 0, 3 );
	}
	
	@Override
	public boolean support( Class<?> valueType ) {
		return ClassUtils.isAssignable( valueType, CharSequence.class );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		String stringValue = value.toString();
		if ( StringUtils.isEmpty( stringValue ) ) {
			return true;
		}
		URL url = null;
		try {
			url = new URL( stringValue );
		} catch ( MalformedURLException e ) {
			return false;
		}
		final String protocol = fragment.getArgument( 0, String.class );
		if ( StringUtils.isNotBlank( protocol ) && !url.getProtocol().equals( protocol ) ) {
			return false;
		}
		final String host = fragment.getArgument( 1, String.class );
		if ( StringUtils.isNotBlank( host ) && !url.getHost().equals( host ) ) {
			return false;
		}
		final Integer port = fragment.getArgument( 1, Integer.class );
		return port == -1 || Objects.equals( url.getPort(), port );
	}
	
}

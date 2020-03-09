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

import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * The annotated {@code CharSequence} must match the specified regular expression.
 * The regular expression follows the Java regular expression conventions
 * see {@link java.util.regex.Pattern}.
 * 
 * <p>Accepts {@code CharSequence}. {@code null} elements are considered valid.
 * 
 * <ul>
 *     <li>Annotation - &#64;Pattern(regexp = "\\d+")</li>
 *     <li>Template rule - "pattern(/[expression]/)"</li>
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see com.viiyue.plugins.validator.constraints.Pattern
 */
public final class PatternHandler extends BaseHandler {
	
	public PatternHandler() {
		super( "pattern" );
		super.setArgumentNumber( 1 );
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
		Pattern pattern = fragment.getArgument( 0, Pattern.class );
		return pattern.matcher( stringValue ).matches();
	}
	
}

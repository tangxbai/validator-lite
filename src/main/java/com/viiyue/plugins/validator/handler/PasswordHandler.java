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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.constraints.Password;
import com.viiyue.plugins.validator.constraints.Password.Level;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * Validates the annotated password string reaches the specified strength.
 * 
 * <p>
 * Password strength supports weak({@link Level#DEFAULT}),
 * strong({@link Level#STRONG}), and general({@link Level#MEDIUM}).
 * 
 * <p>The default is weak({@link Level#DEFAULT}).
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see Password
 * @see Password.Level
 */
public final class PasswordHandler extends BaseHandler {
	
	public PasswordHandler() {
		super( "password" );
		super.setArgumentNumbers( 0, 1 );
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
		Password.Level level = null;
		String levelName = fragment.getArgument( 0, String.class );
		if ( levelName == null ) {
			level = Password.Level.DEFAULT;
			setMessageKeys( context, fragment, "default" ); // {<?>.password.default}
		} else {
			level = Password.Level.valueOf( levelName.toUpperCase( Locale.ENGLISH ) );
			setMessageKeys( context, fragment, levelName ); // {<?>.password.<?>}
		}
		Pattern pattern = level.pattern();
		Matcher matcher = pattern.matcher( stringValue );
		return matcher.matches();
	}

}

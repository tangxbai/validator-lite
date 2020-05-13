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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

/**
 * Common parameter validation handler.
 * 
 * <p>This class contains validations for the following parameters:
 * {@code ip}, {@code qq}, {@code fax}, {@code rar}, {@code ascii},
 * {@code email}, {@code idcard}, {@code postal-code}, {@code letter},
 * {@code letter-uppercase}, {@code letter-lowercase}, {@code chinese},
 * {@code colour}, {@code colour-hex}, {@code colour-rgb}, {@code username},
 * {@code telephone}, {@code cellphone}, {@code int}, {@code int-nagative},
 * {@code int-positive}, {@code decimal}, {@code decimal-nagative},
 * {@code decimal-positive}.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class CommonHanlder extends BaseHandler {

	private static final String[] handlers;
	private static final ConcurrentMap<String, Pattern> patterns;
	static {
		patterns = new ConcurrentHashMap<String, Pattern>( 24 );
		
		patterns.put( "ip", Pattern.compile( "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$" ) );
		patterns.put( "qq", Pattern.compile( "^([1-9][0-9]{4,})$" ) );
		patterns.put( "fax", Pattern.compile( "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$" ) );
		patterns.put( "rar", Pattern.compile( "\\.(rar|zip|7zip|tgz)$" ) );
		patterns.put( "ascii", Pattern.compile( "^[\\x00-\\xFF]+$" ) );
		patterns.put( "email", Pattern.compile( "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$" ) );
		patterns.put( "idcard", Pattern.compile( "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$" ) );
		patterns.put( "postal-code", Pattern.compile( "^\\d{6}$" ) );

		patterns.put( "letter", Pattern.compile( "^[a-zA-Z]+$" ) );
		patterns.put( "letter-lowercase", Pattern.compile( "^[a-z]+$" ) );
		patterns.put( "letter-uppercase", Pattern.compile( "^[A-Z]+$" ) );

		patterns.put( "chinese", Pattern.compile( "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$" ) );

		patterns.put( "colour", Pattern.compile( "^#[a-fA-F0-9]{6}$" ) );
		patterns.put( "colour-hex", Pattern.compile( "^#[a-fA-F0-9]{6}$" ) );
		patterns.put( "colour-rgb", Pattern.compile( "^rgb\\(\\s*[0-255]\\s*,\\s*[0-255]\\s*,\\s*[0-255]\\s*\\)$", Pattern.CASE_INSENSITIVE ) );

		patterns.put( "username", Pattern.compile( "^[\\w\\$\\.-]*$" ) );

		patterns.put( "telephone", Pattern.compile( "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$" ) );
		patterns.put( "cellphone", Pattern.compile( "^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2,5,6,7]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$" ) );
		patterns.put( "mobile", patterns.get( "cellphone" ) );

		patterns.put( "int", Pattern.compile( "^[\\+-]?\\d+$" ) );
		patterns.put( "int-nagative", Pattern.compile( "^-\\d+$" ) );
		patterns.put( "int-positive", Pattern.compile( "^\\+?\\d+$" ) );

		patterns.put( "decimal", Pattern.compile( "^[\\+-]?\\d*\\.\\d+$" ) );
		patterns.put( "decimal-nagative", Pattern.compile( "^-\\d*\\.\\d+$" ) );
		patterns.put( "decimal-positive", Pattern.compile( "^\\+?\\d*\\.\\d+$" ) );
		handlers = patterns.keySet().toArray( new String [ patterns.size() ] );
	}
	
	public CommonHanlder() {
		super( handlers );
	}

	@Override
	public boolean support( Class<?> valueType ) {
		return ClassUtils.isAssignable( valueType, CharSequence.class );
	}

	@Override
	public boolean doValidate( Object value, Fragment fragment, Context context ) {
		String stringValue = value.toString();
		if ( StringUtils.isBlank( stringValue ) ) {
			return true;
		}
		Pattern pattern = patterns.get( fragment.getName() );
		Matcher matcher = pattern.matcher( stringValue );
		return matcher.matches();
	}

}

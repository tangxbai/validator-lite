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
package com.viiyue.plugins.validator.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides some simple string processing functions
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class TextUtil {
	
	private TextUtil() {}
	
	public static boolean isNormalWord( char c ) {
		return c == '_' || c == '-' || c == '$' || Character.isLetterOrDigit( c );
	}
	
	public static boolean isWrappedWith( String text, String prefix, String suffix ) {
		if ( StringUtils.isEmpty( text ) ) return false;
		if ( prefix != null && !text.startsWith( prefix ) ) return false;
		return suffix == null || text.endsWith( suffix );
	}
	
	public static String trimWrapper( String text, String prefix, String suffix ) {
		if ( StringUtils.isEmpty( text) || ( prefix == null && suffix == null ) ) return text;
		int beginIndex = 0, endIndex = 0;
		if ( prefix != null && suffix == null ) {
			beginIndex = prefix.length();
			endIndex = text.length();
		} else if ( prefix == null && suffix != null ) {
			beginIndex = text.length() - suffix.length();
			endIndex = text.length() - 1;
		} else {
			beginIndex = prefix.length();
			endIndex = text.length() - suffix.length();
		}
		return text.substring( beginIndex, endIndex );
	}
	
	public static int length( String value ) {
		try {
			return value == null ? 0 : value.getBytes( "GBK" ).length;
		} catch ( UnsupportedEncodingException e ) {
			return 0;
		}
	}
	
	public static boolean startsWith( String text, Object ... prefixs ) {
		if ( ArrayUtil.isEmpty( prefixs ) ) {
			return true;
		}
		for ( Object prefix : prefixs ) {
			if ( prefix != null && !text.startsWith( prefix.toString() ) ) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean endsWith( String text, Object ... suffixs ) {
		if ( ArrayUtil.isEmpty( suffixs ) ) {
			return true;
		}
		for ( Object suffix : suffixs ) {
			if ( suffix != null && !text.endsWith( suffix.toString() ) ) {
				return false;
			}
		}
		return true;
	}
	
	public static String capitalize( String text ) {
		if ( text == null || text.trim().length() == 0 ) return text;
		int index = 0;
		while ( text.charAt( index ) == ' ' ) {
			index ++;
		}
		char firstChar = text.charAt( index );
		if ( Character.isTitleCase( firstChar ) ) {
			return text;
		}
		return text.substring( 0, index ) + Character.toTitleCase( text.charAt( index ) ) + text.substring( index + 1 );
	}

	public static String uncapitalize( String text ) {
		if ( text == null || text.trim().length() == 0 ) return text;
		int index = 0;
		while ( text.charAt( index ) == ' ' ) {
			index ++;
		}
		char firstChar = text.charAt( index );
		if ( Character.isTitleCase( firstChar ) ) {
			return text;
		}
		return text.substring( 0, index ) + Character.toLowerCase( text.charAt( index ) ) + text.substring( index + 1 );
	}
	
	public static boolean substringMatch( CharSequence str, int index, CharSequence substring ) {
		for ( int j = 0; j < substring.length(); j ++ ) {
			int i = index + j;
			if ( i >= str.length() || str.charAt( i ) != substring.charAt( j ) ) {
				return false;
			}
		}
		return true;
	}

}

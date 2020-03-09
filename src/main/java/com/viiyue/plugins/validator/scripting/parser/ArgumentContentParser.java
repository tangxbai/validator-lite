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
package com.viiyue.plugins.validator.scripting.parser;

import java.util.regex.Pattern;

import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.exception.ExpressionException;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.CacheableContent;
import com.viiyue.plugins.validator.scripting.ExpressionResolver;
import com.viiyue.plugins.validator.utils.MapUtil;

/**
 * Validation rule fragment function parameter parser
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class ArgumentContentParser implements ContentParser {
		
	@Override
	public int order() {
		return 2;
	}
	
	@Override
	public String example() {
		return "(...)";
	}
	
	@Override
	public boolean support( char[] src, char current, int index ) {
		return current == '(';
	}

	@Override
	public int doParser( Fragment fragment, StringBuilder appender, char[] src, int index, int len ) {
		MapContext context = null;
		boolean isEscape = false;
		boolean isPattern = false;
		boolean isSingleQuote = false;
		boolean isDoubleQuote = false;
		boolean isStringArgument = false;
		StringBuffer regexpBuilder = new StringBuffer( 32 );
		for ( index ++; index < len; index ++ ) {
			char c = src[ index ];
			
			// Regular expression
			if ( isPattern ) {
				if ( c == '/' && !isEscape ) {
					isPattern = false;
					Pattern pattern = patternCaches.getOrPut( regexpBuilder.toString(), patternProvider );
					context = new MapContext( MapUtil.newObjectMap( "pattern", pattern ) );
					appender.append( "pattern" ); // Variable placeholder
					regexpBuilder.setLength( 0 );
				} else {
					regexpBuilder.append( c );
				}
				continue;
			}
			
			if ( isStringArgument ) {
				if ( c == '\\' ) {
					isEscape = true;
				}
			} else if ( c == '/' ) {
				isPattern = true;
				continue;
			} else if ( Character.isWhitespace( c ) ) {
				continue;
			} else if ( c == ')' ) {
				String arguments = appender.toString();
				if ( StringUtils.isNotEmpty( arguments ) ) {
					fragment.arguments( ExpressionResolver.resolveArguments( arguments, context ) );
				}
				appender.setLength( 0 );
				break;
			}
			
			if ( c == '\'' ) {
				if ( isEscape ) {
					isEscape = false;
				} else {
					if ( isDoubleQuote ) {
						throw new ExpressionException( "Arguments expression error : \"{0}...\"", new String( src, 0, index + 1 ) );
					}
					isSingleQuote = !isSingleQuote;
					isStringArgument = !isStringArgument;
				}
			} else if ( c == '"' ) {
				if ( isEscape ) {
					isEscape = false;
					if ( isSingleQuote ) {
						throw new ExpressionException( "Arguments expression error : \"{0}...\"", new String( src, 0, index + 1 ) );
					}
				} else {
					isDoubleQuote = !isDoubleQuote;
					isStringArgument = !isStringArgument;
				}
			}
			appender.append( c );
		}
		return index;
	}
	
	private final CacheableContent<String, Pattern> patternCaches = new CacheableContent<String, Pattern>();
	private final CacheableContent.Provider<String, Pattern> patternProvider = new CacheableContent.Provider<String, Pattern>() {
		@Override
		public Pattern create( String regex ) {
			return Pattern.compile( regex );
		}
	};
	
}
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

import com.viiyue.plugins.validator.exception.ExpressionException;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.utils.TextUtil;

/**
 * Rule fragment function name resolver
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class FragmentContentParser implements ContentParser {
	
	@Override
	public int order() {
		return 1;
	}
	
	@Override
	public String example() {
		return "";
	}
	
	@Override
	public boolean support( char[] src, char current, int index ) {
		return TextUtil.isNormalWord( current );
	}

	@Override
	public int doParser( Fragment fragment, StringBuilder appender, char[] src, int index, int len ) {
		for ( ; index < len; index ++ ) {
			char c = src[ index ];
			if ( c == '<' || c == '(' || c == ';' ) {
				fragment.name( appender.toString().trim() );
				appender.setLength( 0 );
				return -- index; // No need to process the current character
			}
			if ( !TextUtil.isNormalWord( c ) ) {
				throw new ExpressionException( "Fragment naming exception: \"{0}\"...", new String( src, 0, index + 1 ) );
			}
			appender.append( c );
		}
		return index;
	}
	
}
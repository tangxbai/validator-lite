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

import com.viiyue.plugins.validator.metadata.Fragment;

/**
 * Validation rule fragment function custom message parser
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class MessageContentParser implements ContentParser {

	@Override
	public int order() {
		return 4;
	}

	@Override
	public String example() {
		return "<<...>>";
	}

	@Override
	public boolean support( char [] src, char current, int index ) {
		return current == '<' && src[ index + 1 ] == '<';
	}

	@Override
	public int doParser( Fragment fragment, StringBuilder appender, char [] src, int index, int len ) {
		for ( index += 2; index < len; index ++ ) {
			char c = src[ index ];
			if ( c == '>' && src[ index + 1 ] == '>' ) {
				fragment.message( appender.toString() );
				appender.setLength( 0 );
				return ++ index;
			}
			appender.append( c );
		}
		return index;
	}

}

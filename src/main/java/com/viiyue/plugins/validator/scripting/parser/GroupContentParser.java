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

/**
 * Validation rule fragment function grouping parser
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class GroupContentParser implements ContentParser {

	private TemplateRuleParser parser;
	
	public GroupContentParser( TemplateRuleParser parser ) {
		this.parser = parser;
	}
	
	@Override
	public int order() {
		return 3;
	}
	
	@Override
	public String example() {
		return "<...>";
	}

	@Override
	public boolean support( char[] src, char current, int index ) {
		return current == '<' && src[ index + 1 ] != '<';
	}

	@Override
	public int doParser( Fragment fragment, StringBuilder appender, char[] src, int index, int len ) {
		return (
			parser.isStrictMode() 
			? doParserByStrictMode( fragment, appender, src, index, len ) 
			: doParserByLooseMode( fragment, appender, src, index, len )
		);
	}
	
	private int doParserByStrictMode( Fragment fragment, StringBuilder appender, char[] src, int index, int len ) {
		boolean hasWhitespace = false;
		boolean isIgnoreWhitespace = true;
		for ( index ++ ; index < len; index ++ ) {
			char c = src[ index ];
			if ( c == '>' ) {
				fragment.addGroup( appender.toString() );
				appender.setLength( 0 );
				break;
			}
			if ( c == ',' ) {
				isIgnoreWhitespace = true;
				fragment.addGroup( appender.toString() );
				appender.setLength( 0 );
			} else if ( Character.isWhitespace( c ) ) {
				if ( !isIgnoreWhitespace && !hasWhitespace ) {
					hasWhitespace = true;
				}
			} else {
				if ( hasWhitespace ) {
					throw new ExpressionException( "Fragment group exception: \"{0}\"...", new String( src, 0, index ) );
				}
				appender.append( c );
				if ( isIgnoreWhitespace ) {
					isIgnoreWhitespace = false;
				}
			}
		}
		return index;
	}
	
	private int doParserByLooseMode( Fragment fragment, StringBuilder appender, char[] src, int index, int len ) {
		for ( index ++ ; index < len; index ++ ) {
			char c = src[ index ];
			if ( c == '>' ) {
				fragment.addGroup( appender.toString() );
				appender.setLength( 0 );
				break;
			}
			if ( c == ',' ) {
				fragment.addGroup( appender.toString() );
				appender.setLength( 0 );
			} else {
				appender.append( c );
			}
		}
		return index;
	}
		
}
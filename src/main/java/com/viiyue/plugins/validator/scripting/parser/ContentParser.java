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
 * Validation Rule Fragmentation Abstract Interface
 * 
 * @author tangxbai
 * @since 1.0.0
 */
interface ContentParser {
	
	/**
	 * Defines which part of the character the parser uses to parse
	 * 
	 * @return parsing order
	 */
	int order();

	/**
	 * Example showing parsing part
	 * 
	 * @return the sample text for parsing part
	 */
	String example();

	/**
	 * Determines whether the current character supports processing
	 * 
	 * @param src the source character array
	 * @param current the current loop character
	 * @param index the current loop index
	 * @return {@code true} to start from the current character, 
	 * and subsequent support is handled by this class until it is actively exited.
	 */
	boolean support( char [] src, char current, int index );

	/**
	 * Handler function to parse the specified part
	 * 
	 * @param fragment the fragment object corresponding to the current validation rule
	 * @param appender the character appender object
	 * @param src the source character array
	 * @param index the current loop index
	 * @param len the total length of the source character array
	 * @return the loop index at the end of processing
	 */
	int doParser( Fragment fragment, StringBuilder appender, char [] src, int index, int len );
	
}

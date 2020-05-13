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
package com.viiyue.plugins.validator.common;

import com.viiyue.plugins.validator.group.All;
import com.viiyue.plugins.validator.resources.Message;

/**
 * Constant definition
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Constants {

	private Constants() {}
	
	// Common
	
	/** Empty string : {@value} */
	public static final String EMPTY_STRING = "";
	/** Space string : {@value} */
	public static final String SAPCE_STRING = " ";
	
	// Separator
	
	/** Separator character : '{@value}' */
	public static final char SEPARATOR = ',';
	/** Separator string : "," */
	public static final String SEPARATOR_TEXT = String.valueOf( SEPARATOR );
	
	/** Group separator character : '{@value}' */
	public static final char DELIMITER = ';';
	/** Group separator string : ";" */
	public static final String DELIMITER_TEXT = String.valueOf( DELIMITER );
	
	// Expression
	
	/** Expression begin token : {@value}  */
	public static final String EXPRESSION_TOKEN_BEGIN = "{";
	/** Expression token separator : {@value}  */
	public static final String EXPRESSION_TOKEN_VALUE_SEPARATOR = ":";
	/** Expression end token : {@value}  */
	public static final String EXPRESSION_TOKEN_END = "}";
	
	// Default message key
	
	/** Message key with missing key : {@value} */
	public static final String MESSAGE_KEY_MISSING_VALUE = "missing-value";
	/** Message key with empty elements : {@value} */
	public static final String MESSAGE_KEY_EMPTY_ELEMENTS = "empty-elements";
	/** Message key with test passed : {@value} */
	public static final String MESSAGE_KEY_TEST_PASSED = "test-passed";
	/** Message key with test rejected : {@value} */
	public static final String MESSAGE_KEY_TEST_REJECTED = "test-rejected";
	
	// Message resource
	
	/** Default message resource key prefix : {@value} */
	public static final String DEFAULT_MESSAGE_KEY_PREFIX = "validator.handler";
	/** Default internationalization resource pack path : "com.viiyue.plugins.validator.resources.Message" */
	public static final String DEFAULT_RESOURCE_NAME = Message.class.getName();
	/** Types of Internationalized Languages Preloaded by Default */
	public static final String[] DEFAULT_MESSAGE_LANGUAGES = { "de", "en", "zh_TW" };
	
	// Group
	
	/** Default grouping : {@link All All.class} */
	public static final Class<?> DEFAULT_GROUP = All.class;
	/** Default class grouping : [ {@link All All.class} ] */
	public static final Class<?>[] DEFAULT_CLASS_GROUPS = new Class<?> [] { DEFAULT_GROUP };
	/** Default object grouping : [ {@link All All.class} ] */
	public static final Object[] DEFAULT_OBJECT_GROUPS = DEFAULT_CLASS_GROUPS;
	/** Default grouping : [ "All" ] */
	public static final String[] DEFAULT_NAME_GROUPS = new String [] { DEFAULT_GROUP.getSimpleName() };

}

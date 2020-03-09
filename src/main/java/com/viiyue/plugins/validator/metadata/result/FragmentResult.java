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
package com.viiyue.plugins.validator.metadata.result;

import java.io.Serializable;

/**
 * Validate rejected element specific rule fragments
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public class FragmentResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fragment;
	private String errorCode;
	private String errorMessage;
	private Object [] arguments;

	public FragmentResult() {}

	public FragmentResult( String fragment, String errorCode, String errorMessage, Object ... arguments ) {
		this.fragment = fragment;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.arguments = arguments;
	}

	public String getFragment() {
		return fragment;
	}

	public void setFragment( String fragment ) {
		this.fragment = fragment;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode( String errorCode ) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage( String errorMessage ) {
		this.errorMessage = errorMessage;
	}

	public Object [] getArguments() {
		return arguments;
	}

	public void setArguments( Object [] arguments ) {
		this.arguments = arguments;
	}

}

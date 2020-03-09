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
package com.viiyue.plugins.validator.metadata;

import com.viiyue.plugins.validator.annotation.When;

/**
 * Conditional validation rule elements
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Conditional {

	private final String[] properties;
	private final When.Result test;

	public Conditional( When when ) {
		this.properties = when.fields();
		this.test = when.is();
	}

	public String[] getProperties() {
		return properties;
	}

	public When.Result getTest() {
		return test;
	}

}

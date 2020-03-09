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
package com.viiyue.plugins.validator.scripting.compiler;

import java.util.List;

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.CacheableContent;

/**
 * Template validation rule compiler
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class TemplateRulesCompiler implements CacheableContent.Provider<String, List<Fragment>> {

	private final ElementCompiler elementCompiler;

	public TemplateRulesCompiler( ElementCompiler elementCompiler ) {
		this.elementCompiler = elementCompiler;
	}

	@Override
	public List<Fragment> create( String template ) {
		return elementCompiler.getTemplateParser().parse( template );
	}

}

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

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.CacheableContent;
import com.viiyue.plugins.validator.scripting.parser.TemplateRuleParser;

/**
 * Validation rule element compilation tool class
 *
 * <p>#ThreadSafe#</p>
 * <p>This class is thread-safe and can run in multi-threaded mode</p>
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see BeanCompiler
 * @see AnnotatedElementCompiler
 * @see TemplateRulesCompiler
 */
public final class ElementCompiler {
	
	private final int initialCapacity = 1 << 8; // 256
	
	// Template expression parser 
	private final TemplateRuleParser templateParser = new TemplateRuleParser();
	
	// Bean class compiler
	private final CacheableContent<Class<?>, List<Element>> elementsCache = new CacheableContent<Class<?>, List<Element>>( initialCapacity );
	private final CacheableContent.Provider<Class<?>, List<Element>> elementProvider = new BeanCompiler( this );
	
	// Field annotation compiler
	private final CacheableContent<AnnotatedElement, List<Fragment>> annotatedFragmentsCache = new CacheableContent<AnnotatedElement, List<Fragment>>( initialCapacity );
	private final CacheableContent.Provider<AnnotatedElement, List<Fragment>> annotatedFragmentProvider = new AnnotatedElementCompiler( this );
	
	// Template rules compiler
	private final CacheableContent<String, List<Fragment>> templatesCache = new CacheableContent<String, List<Fragment>>( initialCapacity );
	private final CacheableContent.Provider<String, List<Fragment>> templateProvider = new TemplateRulesCompiler( this );
	
	public ElementCompiler() {
		super();
	}
	
	public TemplateRuleParser getTemplateParser() {
		return templateParser;
	}
	
	public List<Element> compile( Class<?> beanType ) {
		return elementsCache.getOrPut( beanType, elementProvider );
	}

	public List<Fragment> compile( AnnotatedElement annotated ) {
		return annotatedFragmentsCache.getOrPut( annotated, annotatedFragmentProvider );
	}
	
	public List<Fragment> compile( String rules ) {
		return templatesCache.getOrPut( templateParser.clean( rules ), templateProvider );
	}
	
}

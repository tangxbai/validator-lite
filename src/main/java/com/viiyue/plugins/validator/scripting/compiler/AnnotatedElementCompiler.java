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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.annotation.Mapping;
import com.viiyue.plugins.validator.constraints.Rules;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.provider.AnnotationProvider;
import com.viiyue.plugins.validator.scripting.CacheableContent;
import com.viiyue.plugins.validator.utils.SingletonUtil;

/**
 * Rule compilation for handling Java objects that support annotation elements
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class AnnotatedElementCompiler extends BaseCompiler implements CacheableContent.Provider<AnnotatedElement, List<Fragment>> {

	private final ElementCompiler elementCompiler;

	public AnnotatedElementCompiler( ElementCompiler elementCompiler ) {
		this.elementCompiler = elementCompiler;
	}

	@Override
	public List<Fragment> create( AnnotatedElement element ) {
		// Prefer combo annotations
		Rules rules = getAnnotation( element, Rules.class );
		if ( rules != null ) {
			return elementCompiler.compile( rules.value() );
		}
		
		// Second, parse a single annotation constraint rule.
		List<Fragment> fragments = null;
		final Annotation [] annotations = element.getAnnotations();
		for ( Annotation annotation : annotations ) {
			Fragment fragment = createFragment( annotation );
			if ( fragment != null ) {
				if ( fragments == null ) {
					fragments = new ArrayList<Fragment>( annotations.length - 1 );
				}
				fragments.add( fragment );
			}
		}
		return fragments;
	}
	
	public static Fragment createFragment( Annotation annotation ) {
		Class<? extends Annotation> annotationType = annotation.annotationType();
		Mapping mapping = annotationType.getAnnotation( Mapping.class );
		if ( mapping == null ) {
			return null;
		}
		
		// The default name of the generated fragment
		String defaultName = ( 
			StringUtils.isEmpty( mapping.name() )
			? annotationType.getSimpleName().toLowerCase( Locale.ENGLISH ) // If no name is set, the class name is used by default
			: mapping.name() // Custom fragment name
		);
		
		// Collect parameters via annotations to form validation fragments
		Class<? extends AnnotationProvider<Annotation>> fragmentProviderType = ( Class<? extends AnnotationProvider<Annotation>> ) mapping.provider();
		AnnotationProvider<Annotation> fragmentProvider = SingletonUtil.getBean( fragmentProviderType );
		return fragmentProvider.create( annotation, defaultName );
	}

}

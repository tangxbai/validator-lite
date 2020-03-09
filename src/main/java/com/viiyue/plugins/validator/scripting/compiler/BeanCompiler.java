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

import static com.viiyue.plugins.validator.Validator.LOG;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.viiyue.plugins.validator.annotation.Label;
import com.viiyue.plugins.validator.annotation.Valid;
import com.viiyue.plugins.validator.annotation.When;
import com.viiyue.plugins.validator.constraints.Required;
import com.viiyue.plugins.validator.metadata.Element;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.CacheableContent;
import com.viiyue.plugins.validator.utils.ObjectUtil;

/**
 * Javabean validation rules annotation compiler
 * 
 * @author tangxbai
 * @since 1.0.0
 */
final class BeanCompiler extends BaseCompiler implements CacheableContent.Provider<Class<?>, List<Element>> {

	private final ElementCompiler elementCompiler;

	public BeanCompiler( ElementCompiler elementCompiler ) {
		this.elementCompiler = elementCompiler;
	}

	@Override
	public List<Element> create( Class<?> beanType ) {
		List<Field> fieldsList = FieldUtils.getAllFieldsList( beanType );
		if ( fieldsList.isEmpty() ) {
			return Collections.emptyList();
		}
		final List<Element> elements = new ArrayList<Element>( fieldsList.size() );
		for ( final Field field : fieldsList ) {
			Element element = null;
			// Get field or getter method annotations
			// This operation will take some time and currently cannot be optimized ...
			if ( getAnnotation( field, Valid.class ) == null ) {
				List<Fragment> fragments = elementCompiler.compile( field );
				if ( fragments != null ) {
					element = new Element( field );
					element.setFragments( fragments );
				}
			} else {
				// Cannot be a jdk internal object
				// Of course, it cannot be a collection object such as List or Map.
				final Class<?> fieldBeanType = field.getType();
				final String fieldTypeName = fieldBeanType.getName();
				if ( isSupportType( fieldBeanType ) ) {
					elementCompiler.compile( fieldBeanType );
					element = new Element( field, true );
					Required required = getAnnotation( field, Required.class );
					if ( required != null ) {
						element.setFragments( Arrays.asList( AnnotatedElementCompiler.createFragment( required ) ) );
					}
				}  else {
					LOG.warn( "Skip field '{}' in bean '{}', type cannot be {}", field.getName(), beanType.getName(), fieldTypeName );
				}
			}
			if ( element != null ) {
				element.setConditional( getAnnotation( field, When.class ) );
				element.setLabel( getLabel( beanType, field ) );
				elements.add( element );
			}
		}
		return elements;
	}
	
	/**
	 * <p>
	 * Use the result of the class loader to determine whether the current type
	 * is a custom type.
	 * 
	 * <p>
	 * Generally speaking, the running of a Java program requires at least three
	 * types of loaders to ensure its operation.
	 * 
	 * <ul style="list-style-type: decimal">
	 * <li>Core class library loader: implemented by {@code c/c++}, no corresponding
	 * java class, located in the {@code JRE_HOME/jre/lib} directory.
	 * <li>Extension class library loader: an instance of {@code ExtClassLoader}, located
	 * in the {@code JRE_HOME/jre/lib/ext} directory.
	 * <li>Application class loader: an instance of AppClassLoader.
	 * </ul>
	 * 
	 * <p>Example:
	 * <pre>
	 * &#47;&#47; Core class library loader:
	 * java.lang.Class.class.getClassLoader() -&gt; null;
	 * 
	 * &#47;&#47; Extension class library loader:
	 * com.sun.nio.zipfs.ZipFileSystemProvider.class.getClassLoader() -&gt; sun.misc.Launcher$ExtClassLoader
	 * 
	 * &#47;&#47; Application class loader:
	 * your.package.path.Custom.class.getClassLoader() -&gt; sun.misc.Launcher$AppClassLoader
	 * </pre>
	 * 
	 * @param fieldBeanType the filed bean type
	 * @return {@code true} is the custom bean type, {@code false} is the jdk internal object.
	 */
	private boolean isSupportType( Class<?> fieldBeanType ) {
		final ClassLoader classLoader = fieldBeanType.getClassLoader();
		return Objects.nonNull( classLoader ) && ObjectUtil.isDifferent( classLoader.getClass().getName(), "sun.misc.Launcher$ExtClassLoader" );
	}
	
	/**
	 * <p>
	 * Get the text label of the bean field. If it is not explicitly set with
	 * the annotation {@link Label @Label}, the field name will be returned by default.
	 * 
	 * <p>
	 * <ul>
	 * <li>Use the field name - if not labeled with {@link Label @Label}.
	 * <li>Use the full path of the field - use {@link Label @Label} for labeling, but no specific value is set.
	 * <li>Use specified text - {@link Label @Label}("label text").
	 * </ul>
	 * </p>
	 * 
	 * @param beanType the target java bean type to be validated
	 * @param field the bean field
	 * @return the internationalized label text of the field
	 */
	private String getLabel( Class<?> beanType, Field field ) {
		Label label = getAnnotation( field, Label.class );
		if ( label == null ) {
			return field.getName();
		}
		if ( StringUtils.isEmpty( label.value() ) ) {
			return "{" + beanType.getName() + "." + field.getName() + "}";
		}
		return label.value();
	}
	
}

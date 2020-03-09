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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.viiyue.plugins.validator.annotation.When;
import com.viiyue.plugins.validator.utils.Assert;
import com.viiyue.plugins.validator.utils.MethodUtil;
import com.viiyue.plugins.validator.utils.PropertyUtil;

/**
 * Every element in Javabean that needs to be validated
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class Element {
	
	private final Field field;
	private final String property;
	private final Class<?> beanType;
	private final Class<?> fieldType;
	private final Method getter;
	private final Method setter;
	private final boolean isJavaBean;

	private String label;
	private List<Fragment> fragments;
	private Conditional conditional;
	
	public Element( Field field ) {
		this( field, false );
	}
	
	public Element( Field field, boolean isJavaBean ) {
		this.field = field;
		this.beanType = field.getDeclaringClass();
		this.fieldType = field.getType();
		this.property = field.getName();
		this.isJavaBean = isJavaBean;
		PropertyDescriptor descriptor = PropertyUtil.getDescriptor( this.beanType, this.property );
		Assert.notNull( descriptor != null, "Entity \"{}\" is not a standard java bean", this.beanType );
		this.getter = descriptor.getReadMethod();
		this.setter = descriptor.getWriteMethod();
		Assert.isTrue( getter != null, "Getter method for field \"{0}\" not found", this.property );
	}
	
	// Getter

	public Field getField() {
		return field;
	}

	public String getProperty() {
		return property;
	}

	public Class<?> getBeanType() {
		return beanType;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	public String getLabel() {
		return label;
	}

	public List<Fragment> getFragments() {
		return fragments;
	}
	
	public Conditional getConditional() {
		return conditional;
	}
	
	public boolean isJavaBean() {
		return isJavaBean;
	}
	
	// Setter

	public void setLabel( String label ) {
		this.label = label;
	}

	public void setFragments( List<Fragment> fragments ) {
		if ( this.fragments == null ) {
			this.fragments = fragments;
		}
	}
	
	public void setConditional( When when ) {
		if ( when != null && conditional == null ) {
			this.conditional = new Conditional( when );
		}
	}

	// Helper method

	public boolean isUnconditional() {
		return conditional == null;
	}
	
	public Object getValue( Object instance ) {
		return MethodUtil.invoke( instance, getter );
	}
	
}

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
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.ClassUtils;

/**
 * Validation rejected element object
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class ElementResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String field;
	private String label;
	private Object fieldValue;
	private Object result;
	private boolean isJavaBean;

	public String getField() {
		return field;
	}

	public void setField( String field ) {
		if ( this.field == null ) {
			this.field = field;
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel( String label ) {
		if ( this.label == null ) {
			this.label = label;
		}
	}
	
	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue( Object fieldValue ) {
		if ( this.fieldValue == null ) {
			this.fieldValue = fieldValue;
		}
	}
	
	public boolean isJavaBean() {
		return isJavaBean;
	}

	public void setJavaBean( boolean isJavaBean ) {
		this.isJavaBean = isJavaBean;
	}

	/**
	 * If the current element is a JavaBean, the return value is
	 * {@link ValidatedResult}, and if it is a normal parameter, it is
	 * {@link FragmentResult}.
	 * 
	 * @return the validated element result
	 * @since 1.0.0
	 */
	public Object getResult() {
		return result;
	}
	
	// Removed in v1.0.2
	// |------------------------------------------------------|
	// | public List<FragmentResult> getFragmentResults() {   |
	// |	return ( List<FragmentResult> ) result;           |
	// | }                                                    |
	// |                                                      |
	// | public ValidatedResult getBeanResult() {             |
	// |	return ( ValidatedResult ) result;                |
	// | }                                                    |
	// |------------------------------------------------------|
	
	public void setResult( Object result ) {
		this.result = result;
	}
	
	public boolean isTypeOf( Class<?> target ) {
		if ( result == null ) {
			return false;
		}
		Class<?> resultType = result.getClass();
		if ( ClassUtils.isAssignable( resultType, Collection.class ) ) {
			Iterator<?> iterator = ( ( Collection<?> ) result ).iterator();
			if ( iterator.hasNext() ) {
				resultType = iterator.next().getClass();
			}
		}
		return ClassUtils.isAssignable( resultType, target );
	}

}

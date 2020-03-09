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
package com.viiyue.plugins.validator.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;

import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.utils.Assert;
import com.viiyue.plugins.validator.utils.MethodUtil;

/**
 * The default annotation fragment provider is only applicable to annotations
 * with only {@code groups()} and {@code message()} methods in the annotation.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class DefaultProvider implements AnnotationProvider<Annotation> {
	
	@Override
	public Fragment create( Annotation defined, String defaultName ) {
		Class<? extends Annotation> annotationType = defined.annotationType();
		
		Method groupMethod = MethodUtils.getAccessibleMethod( annotationType, "groups" );
		Assert.notNull( groupMethod, "Can't find groups() method in {0}", annotationType.getName() );
		Method messageMethod = MethodUtils.getAccessibleMethod( annotationType, "message" );
		Assert.notNull( messageMethod, "Can't find message() method in {0}", annotationType.getName() );
		
		Class<?>[] groups = MethodUtil.invoke( defined, groupMethod );
		String message = MethodUtil.invoke( defined, messageMethod );
		return Fragment.of( defaultName ).groups( groups ).message( message );
	}

}

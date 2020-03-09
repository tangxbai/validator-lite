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
package com.viiyue.plugins.validator.scripting;

import java.util.Objects;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Expression placeholder core processing class
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class PropertyPlaceholder implements PropertyPlaceholderHelper.PlaceholderResolver {

	private final JexlContext context;
	private final Object[] arguments;

	public PropertyPlaceholder( JexlContext context, Object ... arguments ) {
		this.context = context == null ? new MapContext() : context;
		this.arguments = arguments;
	}

	@Override
	public String resolvePlaceholder( String placeholder ) {
		Object result = null;
		if ( StringUtils.isNumeric( placeholder ) ) {
			result = arguments[ Integer.parseInt( placeholder ) ]; // {N}
		} else {
			result = ExpressionResolver.resolve( placeholder, context ); // {expression}
		}
		return Objects.toString( result, "" );
	}
	
}

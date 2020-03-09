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

import static com.viiyue.plugins.validator.common.Constants.EXPRESSION_TOKEN_BEGIN;
import static com.viiyue.plugins.validator.common.Constants.EXPRESSION_TOKEN_END;
import static com.viiyue.plugins.validator.common.Constants.EXPRESSION_TOKEN_VALUE_SEPARATOR;

import java.nio.charset.Charset;
import java.util.Collections;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.utils.MapUtil;

/**
 * Validation expression parameter parsing tool class for parsing parameters and
 * expression placeholders in expressions.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class ExpressionResolver {
	
	private ExpressionResolver() {}

	private static final JexlBuilder jexlBuilder = new JexlBuilder().cache( 512 ).strict( true ).silent( true ).charset( Charset.forName( "UTF-8" ) );
	private static final JexlEngine jexlEngine = jexlBuilder.namespaces( MapUtil.newObjectMap( "builder", new Builder() ) ).create();
	private static final PropertyPlaceholderHelper placeholder = new PropertyPlaceholderHelper( EXPRESSION_TOKEN_BEGIN, EXPRESSION_TOKEN_END, EXPRESSION_TOKEN_VALUE_SEPARATOR, true );
	private static final CacheableContent<String, JexlExpression> expressions = new CacheableContent<String, JexlExpression>( 1 << 10 ); // 1024
	private static final CacheableContent.Provider<String, JexlExpression> expressionProvider = new ExpressionProvider();
	
	/**
	 * In order to solve the problem that the direct access speed is very slow
	 * in extreme cases, manually call some methods here to let the VM
	 * initialize their resources.
	 */
	public static final void prepare() {
		ConvertUtils.lookup( Class.class );
		StringUtils.isEmpty( Constants.EMPTY_STRING );
		MapUtils.isEmpty( Collections.EMPTY_MAP );
		ArrayUtils.isEmpty( ArrayUtils.EMPTY_OBJECT_ARRAY );
		CollectionUtils.isEmpty( Collections.EMPTY_LIST );
		JexlContext context = new MapContext( MapUtil.newObjectMap( "now", System.currentTimeMillis() ) );
		resolveArguments( "[ 1, 2L, 1.0f, 1.00, true, 'foo', now ]", context );
	}
	
	public static final Object resolve( String expression, JexlContext context ) {
		return expressions.getOrPut( expression, expressionProvider ).evaluate( context );
	}
	
	public static final Object[] resolveArguments( String expression ) {
		return resolveArguments( expression, null );
	}
	
	public static final Object[] resolveArguments( String expression, JexlContext context ) {
		expression = "builder:toArray(" + expression.trim() + ")";
		JexlExpression exp = expressions.getOrPut( expression, expressionProvider );
		return ( Object[] ) exp.evaluate( context );
	}
	
	public static final String resolveResourceText( String template, JexlContext context, Object ... arguments ) {
		if ( placeholder.canParsing( template ) ) {
			return placeholder.replacePlaceholders( template, new PropertyPlaceholder( context, arguments ) );
		}
		return template;
	}
	
	public static class Builder {
		public Object[] toArray( Object ... args ) {
			return args;
		}
	}
	
	private static class ExpressionProvider implements CacheableContent.Provider<String, JexlExpression> {
		@Override
		public JexlExpression create( String expression ) {
			return ExpressionResolver.jexlEngine.createExpression( expression );
		}
	}
	
}

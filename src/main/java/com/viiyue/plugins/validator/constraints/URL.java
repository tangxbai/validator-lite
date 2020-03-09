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
package com.viiyue.plugins.validator.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.viiyue.plugins.validator.annotation.Mapping;
import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.constraints.URL.UrlProvider;
import com.viiyue.plugins.validator.handler.URLHandler;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.provider.AnnotationProvider;

/**
 * Validates the annotated string is an URL.
 *
 * <p>
 * The parameters {@code protocol}, {@code host} and {@code port} are matched against the corresponding parts of the URL.
 * and an additional regular expression can be specified using {@code regexp} and {@code flags} to further restrict the
 * matching criteria.
 * </p>
 *
 * <p>
 * <b>Note</b>:
 * Per default the constraint validator for this constraint uses the {@code java.net.URL} constructor to validate the string.
 * This means that a matching protocol handler needs to be available. Handlers for the following protocols are guaranteed
 * to exist within a default JVM - http, https, ftp, file, and jar.
 * See also the Javadoc for <a href="http://docs.oracle.com/javase/7/docs/api/java/net/URL.html">URL</a>.
 * </p>
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see URLHandler
 */
@Target({ PARAMETER, FIELD, METHOD })
@Retention( RUNTIME )
@Documented
@Mapping( usage = "url | url(?<protocol>) | url(?<protocol>, ?<host>) | url(?<protocol>, ?<host>, ?<port>)", provider = UrlProvider.class )
public @interface URL {
	
	String protocol() default Constants.EMPTY_STRING;
	String host() default Constants.EMPTY_STRING;
	int port() default -1;
	
	Class<?>[] groups() default {};
	String message() default Constants.EMPTY_STRING;
	
	class UrlProvider implements AnnotationProvider<URL> {
		@Override
		public Fragment create( URL defined, String defaultName ) {
			return Fragment.of( defaultName )
				.groups( defined.groups() )
				.message( defined.message() )
				.arguments( defined.protocol(), defined.host(), defined.port() );
		}
	}
	
}

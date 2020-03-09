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
import java.util.Locale;
import java.util.regex.Pattern;

import com.viiyue.plugins.validator.annotation.Mapping;
import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.constraints.Password.PasswordProvider;
import com.viiyue.plugins.validator.handler.PasswordHandler;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.provider.AnnotationProvider;

/**
 * Validates the annotated password string reaches the specified strength.
 * 
 * <p>
 * Password strength supports weak({@link Level#DEFAULT}),
 * strong({@link Level#STRONG}), and general({@link Level#MEDIUM}).
 * 
 * <p>The default is weak({@link Level#DEFAULT}).
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see PasswordHandler
 */
@Target({ PARAMETER, FIELD, METHOD })
@Retention( RUNTIME )
@Documented
@Mapping( usage = "password(?)", provider = PasswordProvider.class )
public @interface Password {
	
	Level level();
	
	Class<?>[] groups() default {};
	String message() default Constants.EMPTY_STRING;
	
	class PasswordProvider implements AnnotationProvider<Password> {
		@Override
		public Fragment create( Password defined, String defaultName ) {
			return Fragment.of( defaultName )
				.groups( defined.groups() )
				.message( defined.message() )
				.arguments( defined.level().name().toLowerCase( Locale.ENGLISH ) );
		}
	}
	
	/**
	 * Supported password levels
	 * 
	 * 
	 * @author tangxbai
	 * @since 1.0.0
	 * 
	 * @see Level#DEFAULT
	 * @see Level#MEDIUM
	 * @see Level#STRONG
	 */
	public static enum Level {
		
		/**
		 * Weak password
		 * <p>
		 * It can be numbers, characters, or special characters.
		 */
		DEFAULT( Pattern.compile( "^(?:\\d+|[a-zA-Z]+|[!@#$%^&*.]+)$" ) ), 

		/**
		 * General password
		 * <p>
		 * It cannot be all letters, numbers, and special characters. 
		 * Must contain at least two types of characters.
		 */
		MEDIUM( Pattern.compile( "^(?![a-zA-Z]+$)(?!\\d+$)(?![!@#$%^&*.]+$)[a-zA-Z\\d!@#$%^&*.]+$" ) ), 

		/**
		 * Strong password
		 * <p>
		 * It cannot be all letters, numbers, and special characters. 
		 * Must contain characters such as special characters,
		 * numbers, and letters.
		 */
		STRONG( Pattern.compile( "^(?![a-zA-Z]+$)(?!\\d+$)(?![!@#$%^&*.]+$)(?![a-zA-Z\\d]+$)(?![a-zA-Z!@#$%^&*.]+$)(?![\\d!@#$%^&*.]+$)[a-zA-Z\\d!@#$%^&*.]+$" ) );
		
		private final Pattern pattern;

		private Level( Pattern pattern ) {
			this.pattern = pattern;
		}
		
		public Pattern pattern() {
			return pattern;
		}
		
	}
	
}

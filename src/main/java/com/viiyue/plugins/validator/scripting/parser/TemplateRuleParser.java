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
package com.viiyue.plugins.validator.scripting.parser;

import static com.viiyue.plugins.validator.common.Constants.DELIMITER;
import static com.viiyue.plugins.validator.common.Constants.DELIMITER_TEXT;
import static com.viiyue.plugins.validator.common.Constants.SEPARATOR_TEXT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.viiyue.plugins.validator.exception.ExpressionException;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.utils.ObjectUtil;
import com.viiyue.plugins.validator.utils.StringAppender;

/**
 * Template validation rule parser, supports strict mode and loose mode parsing.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class TemplateRuleParser {
	
	private boolean strictMode;
	private final int parserSize;
	private final int defaultSize;
	private final List<ContentParser> parsers;
	private final Comparator<ContentParser> comparator = new Comparator<ContentParser>() {
		@Override
		public int compare( ContentParser o1, ContentParser o2 ) {
			return o1.order() - o2.order();
		}
	};
	
	public TemplateRuleParser() {
		this( false );
	}
	
	public TemplateRuleParser( boolean strictMode ) {
		this( 1 << 4, strictMode );
	}
	
	public TemplateRuleParser( int defaultSize ) {
		this( defaultSize, false );
	}
	
	public TemplateRuleParser( int defaultSize, boolean strictMode ) {
		this.defaultSize = defaultSize;
		this.strictMode = strictMode;
		this.parsers = loadParsers();
		this.parserSize = parsers.size();
	}
	
	public void setStrictMode( boolean strictMode ) {
		this.strictMode = strictMode;
	}

	public boolean isStrictMode() {
		return this.strictMode;
	}

	public String clean( String input ) {
		if ( input == null ) {
			throw new ExpressionException( "Expression cannot be empty" );
		}
		input = input.trim();
		if ( input.isEmpty() ) {
			throw new ExpressionException( "Expression cannot be blank" );
		}
		return appendDelimiterIfMissing( input );
	}
	
	public List<Fragment> parse( String input ) {
		return isStrictMode() ? parseByStrictMode( input ) : parseByLooseMode( input );
	}

	private List<Fragment> parseByStrictMode( String input ) {
		if ( StringUtils.isEmpty( input ) ) {
			return Collections.emptyList();
		}
		Fragment fragment = null;
		final char [] src = input.toCharArray();
		final StringBuilder appender = new StringBuilder( 1 << 10 ); // 1024
		final List<Fragment> fragments = new ArrayList<Fragment>( defaultSize ); // Specify size
		final StringAppender orders = new StringAppender( parserSize * 2 ); 
		final List<ContentParser> chains = new ArrayList<ContentParser>( parserSize ); // Maximum size
		for ( int i = 0, len = src.length; i < len; i ++ ) {
			final char c = src[ i ];
			if ( c == DELIMITER ) {
				checkExpression( fragment, orders, chains );
				fragments.add( fragment );
				fragment = null;
				chains.clear();
				continue;
			}
			for ( ContentParser parser : parsers ) {
				if ( parser.support( src, c, i ) ) {
					if ( fragment == null ) {
						fragment = new Fragment();
					}
					chains.add( parser ); // Call chain
					i = parser.doParser( fragment, appender, src, i, len );
					break;
				}
			}
		}
		return fragments;
	}
	
	
	private List<Fragment> parseByLooseMode( String input ) {
		if ( StringUtils.isEmpty( input ) ) {
			return Collections.emptyList();
		}
		Fragment fragment = null;
		final char [] src = input.toCharArray();
		final StringBuilder appender = new StringBuilder( 1 << 10 ); // 1024
		final List<Fragment> fragments = new ArrayList<Fragment>( defaultSize ); // Specify size
		for ( int i = 0, len = src.length; i < len; i ++ ) {
			final char c = src[ i ];
			if ( c == DELIMITER ) {
				fragments.add( fragment );
				fragment = null;
				continue;
			}
			for ( ContentParser parser : parsers ) {
				if ( parser.support( src, c, i ) ) {
					if ( fragment == null ) {
						fragment = new Fragment();
					}
					i = parser.doParser( fragment, appender, src, i, len );
					break;
				}
			}
		}
		return fragments;
	}
	
	private void checkExpression( Fragment fragment, StringAppender orders, List<ContentParser> parsers ) {
		if ( fragment == null ) return;
		String actualOrder = getOrders( orders, parsers );
		parsers.sort( comparator );
		String normalOrder = getOrders( orders, parsers );
		if ( ObjectUtil.isDifferent( actualOrder, normalOrder ) ) {
			throw new ExpressionException( "The format of the expression \"{0}\" is incorrect, it should be: \"{0}{1}\"", fragment.getName(), getExamples( parsers ) );
		}
	}
	
	private String getOrders( StringAppender orders, List<ContentParser> parsers ) {
		for ( int i = 0, s = parsers.size(); i < s; i ++ ) {
			orders.addDelimiter( SEPARATOR_TEXT ).append( parsers.get( i ).order() );
		}
		String order = orders.toString();
		orders.reset();
		return order;
	}
	
	private String getExamples( List<ContentParser> parsers ) {
		StringBuilder examples = new StringBuilder( parsers.size() );
		for ( int i = 0, s = parsers.size(); i < s; i ++ ) {
			examples.append( parsers.get( i ).example() );
		}
		return examples.toString();
	}
	
	private String appendDelimiterIfMissing( String input ) {
		if ( input.charAt( input.length() - 1 ) != DELIMITER ) {
			input += DELIMITER_TEXT;
		}
		return input;
	}
	
	private List<ContentParser> loadParsers() {
		return Arrays.asList( 
			new FragmentContentParser(), // name
			new ArgumentContentParser(), // name(?...)
			new GroupContentParser( this ), // name<?>
			new MessageContentParser() // name<<?>>
		);
	}

}

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Validated data result object
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class ValidatedResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean passed;
	private int totalCount;
	private int passedCount;
	private int errorCount;
	private int ignoredCount;
	private String message;
	private final List<ElementResult> rejectedResults = new ArrayList<ElementResult>( 1 << 3 );
	
	public ValidatedResult() {
		this( true );
	}
	
	public ValidatedResult( boolean passed ) {
		this( passed, null );
	}
	
	public ValidatedResult( boolean passed, String message ) {
		this.passed = passed;
		this.message = message;
	}
	
	// Getter/Setter
	
	public boolean isPassed() {
		return passed;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount( int totalCount ) {
		this.totalCount = totalCount;
	}
	
	public int getPassedCount() {
		return passedCount;
	}

	public int getIgnoredCount() {
		return ignoredCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage( String message ) {
		this.message = message;
	}

	public List<ElementResult> getRejectedResults() {
		return Collections.unmodifiableList( rejectedResults );
	}
	
	// Helper

	public ValidatedResult passedAccumulation() {
		this.passedCount ++;
		return this;
	}
	
	public ValidatedResult ignoredAccumulation() {
		this.ignoredCount ++;
		return this;
	}
	
	public ValidatedResult merge( ValidatedResult result ) {
		this.totalCount += result.totalCount;
		this.passedCount += result.passedCount;
		this.ignoredCount += result.ignoredCount;
		if ( !result.isPassed() ) {
			this.passed = false;
			this.errorCount += result.errorCount;
			this.rejectedResults.addAll( result.rejectedResults );
		}
		return this;
	}
	
	public ValidatedResult addRejectedResult( ElementResult rejectedResult ) {
		this.passed = false;
		this.errorCount ++;
		this.rejectedResults.add( rejectedResult );
		return this;
	}
	
	public ElementResult getFirstRejectedResult() {
		return rejectedResults.size() >= 1 ? rejectedResults.get( 0 ) : null;
	}
	
	public ElementResult getLastRejectedResult() {
		int size = rejectedResults.size();
		return size >= 1 ? rejectedResults.get( size - 1 ) : null;
	}
	
	// Static helper method
	
	public static final ValidatedResult empty( String message ) {
		return new ValidatedResult( true, message );
	}

}

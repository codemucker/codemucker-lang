/*
 * Copyright 2011 Bert van Brakel
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
package com.bertvanbrakel.lang.matcher;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import static org.apache.commons.lang3.Validate.*;

public class EqualsMatcher<T> extends TypeSafeMatcher<T>{

	private final T expect;
	private final String[] exclude;

	public EqualsMatcher(T expect) {
		this(expect,null);
	}

	public EqualsMatcher(T expect, String[] exclude) {
		super();
		notNull(expect);
		this.expect = expect;
		this.exclude = exclude;
	}
	
	public static <T> EqualsMatcher<T> equals(T expect, String[] excudeFields){
		return new EqualsMatcher<T>(expect, excudeFields);
	}
	
	public static <T> EqualsMatcher<T> equalsAllFields(T expect){
		return new EqualsMatcher<T>(expect, null);
	}
	

	@Override
	public void describeTo(Description desc) {
		String s = ToStringBuilder.reflectionToString(expect,ToStringStyle.SHORT_PREFIX_STYLE);
		desc.appendText("expect " + s + ", excluding fields " + exclude);	
	}

	@Override
	public boolean matchesSafely(T item) {
		return EqualsBuilder.reflectionEquals(expect, item, exclude);
	}

	public static <T> EqualsMatcher<T> reflectionEquals(T expect) {
		return new EqualsMatcher<T>(expect);
	}

	public static <T> EqualsMatcher<T> reflectionEqualsIgnoringFields(T expect, String[] ignoreFields) {
		return new EqualsMatcher<T>(expect, ignoreFields);
	}
}

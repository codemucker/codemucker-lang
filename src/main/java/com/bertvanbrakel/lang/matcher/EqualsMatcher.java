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

}

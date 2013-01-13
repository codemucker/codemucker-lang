package com.bertvanbrakel.lang.matcher;

public abstract class AbstractMatcher<T> implements Matcher<T> {
	
	@Override
	public final boolean matches(T actual) {
		return matches(actual, NullMatchContext.INSTANCE);
	}
	
	@Override
	public void describeTo(Description desc) {
	}
	
	@Override
	public String toString(){
		Description d = new DefaultDescription();
		describeTo(d);
		return d.toString();
	}
}

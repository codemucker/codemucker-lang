package com.bertvanbrakel.lang.matcher;

public abstract class AbstractNotNullMatcher<T> extends AbstractMatcher<T> {
	
	public final boolean matches(T actual, MatchDiagnostics ctxt){
		if( actual == null){
			ctxt.text("expect not null");
			return false;
		}
		return matchesSafely(actual);
	}

	protected abstract boolean matchesSafely(T actual);
}

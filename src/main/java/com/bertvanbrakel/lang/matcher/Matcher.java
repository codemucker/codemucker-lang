package com.bertvanbrakel.lang.matcher;

public interface Matcher<T> extends SelfDescribing {

	public boolean matches(T actual,MatchDiagnostics ctxt);
	public boolean matches(T actual);
}

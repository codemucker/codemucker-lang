package com.bertvanbrakel.lang.matcher;

public interface MatchDiagnostics extends Description {
	<T> boolean TryMatch(T actual, Matcher<T> matcher);
}

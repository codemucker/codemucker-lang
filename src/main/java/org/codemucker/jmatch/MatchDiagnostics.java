package org.codemucker.jmatch;

public interface MatchDiagnostics extends Description {
	<T> boolean TryMatch(T actual, Matcher<T> matcher);
}

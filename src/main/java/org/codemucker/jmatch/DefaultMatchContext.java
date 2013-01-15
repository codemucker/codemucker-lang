package org.codemucker.jmatch;

public class DefaultMatchContext extends DefaultDescription implements MatchDiagnostics {

	@Override
	public <T> boolean TryMatch(T actual, Matcher<T> matcher) {
		return matcher.matches(actual, this);
	}
}

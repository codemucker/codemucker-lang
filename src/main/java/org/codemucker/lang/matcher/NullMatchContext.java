package org.codemucker.lang.matcher;

public class NullMatchContext extends NullDescription implements MatchDiagnostics {

	public static final MatchDiagnostics INSTANCE = new NullMatchContext();
	
	@Override
	public <T> boolean TryMatch(T actual, Matcher<T> matcher) {
		return matcher.matches(actual, this);
	}
}

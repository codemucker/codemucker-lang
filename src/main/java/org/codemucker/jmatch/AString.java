package org.codemucker.lang.matcher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AString {

	@SafeVarargs
	public static final List<Matcher<String>> equalToAll(final String... expectAll) {
		List<Matcher<String>> matchers = new ArrayList<Matcher<String>>(
				expectAll.length);
		for (int i = 0; i < expectAll.length; i++) {
			matchers.add(equalTo(expectAll[i]));
		}
		return matchers;
	}
	
	public static final Matcher<String> equalTo(final String expect){
		return new AbstractMatcher<String>(){ 
			@Override
			public boolean matches(String found, MatchDiagnostics ctxt) {
				if( expect == null && found == null){
					return true;
				}
				if( expect == null || found == null){
					return false;
				}
				return expect.equals(found);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string equal to '%1'", expect);
			}
		};
	}

	public static final Matcher<String> notEmpty(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return !found.isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a non empty or null string");
			}
		};
	}

	public static final Matcher<String> empty(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return found.isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an empty string");
			}
		};
	}
	
	public static final Matcher<String> notBlank(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return !found.trim().isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string not null or blank");
			}
		};
	}
	
	public static final Matcher<String> blank(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return found.trim().isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a non null blank string");
			}
		};
	}
	
	public static final Matcher<String> blankOrNull(){
		return new AbstractMatcher<String>(){ 
			@Override
			public boolean matches(String found, MatchDiagnostics ctxt) {
				return found == null || found.trim().isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a blank or null string");
			}
		};
	}
	
	public static final Matcher<String> endingWith(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return found.endsWith(expect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string ending with '%1'", expect);
			}
		};
	}
	
	public static final Matcher<String> endingWithIgnoreCase(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			final String lowerExpect = expect.toLowerCase();
				
			@Override
			public boolean matchesSafely(String found) {
				return found.toLowerCase().endsWith(lowerExpect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string ending with '%1' (ignoring case)", expect);
			}
		};
	}
	
	public static final Matcher<String> contains(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return found.contains(expect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string containing '%1'", expect);
			}
		};
	}
	
	public static final Matcher<String> containsIgnoreCase(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			final String lowerExpect = expect.toLowerCase();
				
			@Override
			public boolean matchesSafely(String found) {
				return found.toLowerCase().contains(lowerExpect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string containing '%1' (ignoring case)", expect);
			}
		};
	}
	
	public static final Matcher<String> startingWith(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found) {
				return found.startsWith(expect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string starting with '%1'", expect);
			}
		};
	}
	
	public static final Matcher<String> startingWithIgnoreCase(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			final String lowerExpect = expect.toLowerCase();
				
			@Override
			public boolean matchesSafely(String found) {
				return found.toLowerCase().startsWith(lowerExpect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string starting with '%1' (ignoring case)", expect);
			}
		};
	}
	
	public static Matcher<String> withAntPattern(String antExpression){
		return new RegExpPatternMatcher(antExpToPattern(antExpression));
	}
	
	public static Matcher<String> withPattern(String pattern){
		return new RegExpPatternMatcher(Pattern.compile(pattern));
	}
	
	public static Matcher<String> withPattern(Pattern pattern){
		return new RegExpPatternMatcher(pattern);
	}
	
	/**
	 * Convert an ant regular expression into a java pattern
	 */
	private static Pattern antExpToPattern(String antPattern) {
		return Pattern.compile(antExpToPatternExp(antPattern));
	}
	
	/**
	 * Convert an ant regular expression to a standard java pattern expression
	 */
	public static String antExpToPatternExp(String antPattern) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < antPattern.length(); i++) {
    		char c = antPattern.charAt(i);
    		if (c == '.') {
    			sb.append("\\.");
    		} else if (c == '*') {
    			sb.append(".*");
    		} else if (c == '?') {
    			sb.append(".?");
    		} else {
    			sb.append(c);
    		}
    	}
    	return sb.toString();
    }

	
	private static class RegExpPatternMatcher extends AbstractNotNullMatcher<String> {
		private final Pattern pattern;
		

		public RegExpPatternMatcher(Pattern pattern) {
			this.pattern = checkNotNull(pattern,"expect regexp pattern");
		}

		@Override
		public boolean matchesSafely(String s) {
			return pattern.matcher(s).matches();
		}
		
		@Override
		public void describeTo(Description desc) {
			super.describeTo(desc);
			desc.text("a string matching reg exp '%s'", pattern.pattern());
		}
	}
	
}

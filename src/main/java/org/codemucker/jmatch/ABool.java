package org.codemucker.jmatch;

public class ABool {

	public static final Matcher<Boolean> isTrue(){
		return equalTo(true);
	}
	
	public static final Matcher<Boolean> isFalse(){
		return equalTo(false);
	}
	
	public static final Matcher<Boolean> equalTo(final Boolean expect){
		return new AbstractMatcher<Boolean>() {
			@Override
			public boolean matches(Boolean found, MatchDiagnostics ctxt) {
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
				desc.text("a bool equal to " + Boolean.valueOf(expect));
			}
		};
	}
}

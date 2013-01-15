package org.codemucker.jmatch;

public class MatchException  extends RuntimeException {

	private static final long serialVersionUID = -6502822550004124880L;

	public MatchException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MatchException(String msg) {
		super(msg);
	}

}

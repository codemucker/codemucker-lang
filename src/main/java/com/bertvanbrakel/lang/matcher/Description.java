package com.bertvanbrakel.lang.matcher;

public interface Description {

	Description text(String string);
	Description text(String string,Object arg1);
	Description text(String string,Object arg1,Object arg2);
	Description text(String string,Object arg1,Object arg2,Object arg3);
	Description text(String string,Object... args);
	
	Description value(String label, Object value);
	Description values(String label, Iterable<?> values);
	Description child(Object child);
	
	/**
	 * Return whether this is a null description. That is, a descroption which ignores all input
	 * @return
	 */
	boolean isNull();
	
}

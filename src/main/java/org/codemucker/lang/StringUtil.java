package org.codemucker.lang;

public class StringUtil {

    public static String lowerFirstChar(String name) {
    	if (name.length() > 1) {
    		return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    	} else {
    		return Character.toLowerCase(name.charAt(0)) + "";
    	}
    }

    public static String upperFirstChar(String name) {
    	if (name.length() > 1) {
    		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    	} else {
    		return Character.toUpperCase(name.charAt(0)) + "";
    	}
    }

}

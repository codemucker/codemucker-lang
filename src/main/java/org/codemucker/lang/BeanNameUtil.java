package org.codemucker.lang;

public class BeanNameUtil {

	public static String toSetterName(String name) {
		name = stripPrefix(name);
		return "set" + ClassNameUtil.upperFirstChar(name);
	}

	public static String toGetterName(String name, java.lang.reflect.Type type) {
		boolean isBool = "boolean".equals(type.toString())|| "java.lang.Boolean".equals(type.toString());
		return toGetterName(name, isBool);
	}

	public static String toGetterName(String name, Class<?> type) {
		return toGetterName(name, boolean.class.equals(type) || Boolean.class.equals(type));
	}

	public static String toGetterName(String name, boolean isBoolean) {
		name = stripPrefix(name);
		return (isBoolean ? "is" : "get") + ClassNameUtil.upperFirstChar(name);
	}

	/**
	 * Strips the get/set/is prefix from a method name 
	 * 
	 * <p>Handles the case where the prefix get/set/is is actually part of a name by ensuring that the letter after the prefix is in uppercase</p>
	 * 
	 * <p>Examples:
	 * 	<ul>
	 * 		<li>getFoo,setFoo,isFoo --%gt;  'Foo'
	 * 		<li>gettyburg --&gt; gettyburg (unchanged)
	 * 		<li>iso --&gt; iso (unchanged)
	 * 		<li>getIso--&gt; iso
	 * 		<li>isIso--&gt; iso
	 * 		<li>isolated --&gt; isolated (unchanged)
	 * 		<li>isIsolated --&gt; isolated
	 * </p>
	 * @param name
	 * @return
	 */
	public static String stripPrefix(String name) {
		if (name.startsWith("get") || name.startsWith("set") && (name.length() > 3  && Character.isUpperCase(name.charAt(4)))) {
			return name.substring(3);
		}
		if (name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(3))) {
			return name.substring(2);
		}
		return name;
	}

}

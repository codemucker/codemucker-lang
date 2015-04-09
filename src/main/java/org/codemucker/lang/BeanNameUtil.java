package org.codemucker.lang;

public class BeanNameUtil {

	public static String toSetterName(String name) {
		return addPrefixName("set",name);
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
		return addPrefixName((isBoolean ? "is" : "get"),name);
	}

	public static String addPrefixName(String prefix,String name) {
		name = stripPrefix(name);
		return prefix + ClassNameUtil.upperFirstChar(name);
	}

	/**
	 * Best effort basis to convert the given method name to a property name
	 * 
	 * <p>
	 * <ul>
	 * 	<li>setFoo --&gt; foo</li>
	 * 	<li>Foo --&gt; foo</li>
	 *  <li>F --&gt; f</li>
	 *  <li>setFOO --&gt; fOO</li>
	 * </ul>
	 * </p>
	 */
	public static String methodToPropertyName(String name) {
		return lowerFirst(stripPrefix(name));
	}
	
	/**
	 * Best effort basis to convert the given method name to a property name
	 */
	public static String fieldToPropertyName(String name) {
		return lowerFirst(name);
	}
	
	public static String lowerFirst(String name) {
		if(name.length() > 1){
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);		
		} else if(name.length() == 1){
			name = Character.toLowerCase(name.charAt(0)) + "";		
		}
		return name;
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
		if ((name.startsWith("get") || name.startsWith("set")) && (name.length() > 3  && Character.isUpperCase(name.charAt(3)))) {
			return name.substring(3);
		}
		if (name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2))) {
			return name.substring(2);
		}
		return name;
	}


	public static String extractIndexedKeyType(String fullType) {
		//eg ..Map or ..Map<String,Bar>  or ..Map<String,Bar<T,Foo>>
		int first = fullType.indexOf('<');
		if (first != -1) {
			int last = fullType.indexOf(',', first);
			if(last !=-1){
				return fullType.substring(first + 1, last);
			} else {
				return null;//have a generic part but not enough generic params for a key
			}
		} else {
			return "java.lang.Object";
		}
	}

	public static String extractIndexedValueType(String fullType) {
		//eg .. List or ..List<String>  or ..List<Bar<T,Foo>> or ..Map or ..Map<String,Bar>  or ..Map<String,Bar<T,Foo>>
		int first = fullType.indexOf('<');
		if (first != -1) {
			int angle = fullType.indexOf('<', first+1);
			int comma = fullType.indexOf(',', first+1);
			if( angle !=-1 || comma != -1){
				if(angle == -1){
					first = comma; 
				} else if( comma == -1){
					first = angle;
				} else if( angle < comma){
					first = angle;
				} else {
					first = comma;
				}
			}
			int last = fullType.lastIndexOf('>');
			return fullType.substring(first + 1, last);
		}
		return "java.lang.Object";
	}
    

}

package org.codemucker.lang;

public class BeanNameUtil {

    public static String toSetterName(String name) {
        return "set" + ClassNameUtil.upperFirstChar(name);
    }

    public static String toGetterName(String name, java.lang.reflect.Type type) {
        boolean isBool = "boolean".equals(type.toString()) || "java.lang.Boolean".equals(type.toString());
        return toGetterName(name, isBool);
    }

    public static String toGetterName(String name, Class<?> type) {
        return toGetterName(name, boolean.class.equals(type) || Boolean.class.equals(type));
    }

    public static String toGetterName(String name, boolean isBoolean) {
        return (isBoolean ? "is" : "get") + ClassNameUtil.upperFirstChar(name);
    }

}

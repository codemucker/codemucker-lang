package com.bertvanbrakel.lang.matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.base.Preconditions;

public class AProperty {
	public static <TProperty> Matcher<TProperty> withNameMatches(String propertyName,Matcher<TProperty> matcher){
		return null;
	}
	
	public static <T> PropertyMatcherBuilder<T> on(Class<T> bean){
		return new PropertyMatcherBuilder<T>(bean);
	}
	
	public static class PropertyMatcherBuilder<T> {

		private static final Class<?>[] EMPTY_PARAMS = new Class[]{};
		private final Class<T> beanClass;
		private Method getter;
		
		public PropertyMatcherBuilder(Class<T> beanClass) {
			this.beanClass = beanClass;
		}
		
		public PropertyMatcherBuilder<T> named(String propertyName){
			//TODO:cache these?
			//todo:loop through getter names
			Preconditions.checkNotNull(propertyName,"expect non null propertyName");
			Method getter;
			if( propertyName.startsWith("get")){
				getter = getMethod(propertyName);
			} else if( propertyName.startsWith("is")){
				getter = getMethod(propertyName);
			} else {
				String upperFirst = toUpperFirst(propertyName);
				getter = getMethod("get" + upperFirst);
				if( getter == null ){
					getter = getMethod("is" + upperFirst);
				}
			}
			if( getter == null){
				throw new IllegalArgumentException("could not find a getter method for property '" + propertyName + "'");
			}
			if( Void.class.equals(getter.getReturnType())){
				throw new IllegalArgumentException("getter method for property '" + propertyName + "' returns void. Expected a return value");
			}
			
			this.getter = getter;
			return this;
		
		}
		
		public <TPropertyType> Matcher<T> equals(Matcher<TPropertyType> matcher){
			return new GetterMethodMatcher<T>(getter,matcher);
		}

		private Method getMethod(String name){
			try {
				return beanClass.getMethod(name, EMPTY_PARAMS);
			} catch (NoSuchMethodException e) {
				return null;
			} catch (SecurityException e) {
				throw new IllegalArgumentException("Method with name" + name + " is not accessible", e);
			}
		}
		
		private static String toUpperFirst(String s){
			if( s.length() == 1){
				return s.toUpperCase();
			}
			return Character.toUpperCase(s.charAt(0)) + s.substring(1,s.length());
		}
		
		public Matcher<T> build(){
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		private static class GetterMethodMatcher<T> extends AbstractNotNullMatcher<T>{
			private final Method getterMethod;
			private final Matcher propertyMatcher;
			private static final Object[] EMPTY_ARGS = new Object[]{};
			
			GetterMethodMatcher(Method m,Matcher propertyMatcher){
				this.getterMethod = m;
				this.propertyMatcher = propertyMatcher;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			protected boolean matchesSafely(T actual) {
				try {
					Object propertyVal = getterMethod.invoke(actual, EMPTY_ARGS);
					return propertyMatcher.matches(propertyVal);
				} catch (IllegalAccessException e) {
					throw new MatchException("error invoking getter",e);
				} catch (IllegalArgumentException e) {
					throw new MatchException("error invoking getter",e);
				} catch (InvocationTargetException e) {
					throw new MatchException("error invoking getter",e);
				}
			}
		}
	}
}

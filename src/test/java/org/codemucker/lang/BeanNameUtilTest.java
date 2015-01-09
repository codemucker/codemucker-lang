package org.codemucker.lang;

import org.junit.Assert;
import org.junit.Test;

public class BeanNameUtilTest {

	@Test
	public void toGetterName(){
		for(String name:new String[]{"getFoo", "foo", "Foo", "setFoo","isFoo"}){
			Assert.assertEquals("for " + name,"getFoo",BeanNameUtil.toGetterName(name, false));
			Assert.assertEquals("for " + name,"isFoo",BeanNameUtil.toGetterName(name, true));
		}
		for(String name:new String[]{"getter"}){
			Assert.assertEquals("for " + name,"getGetter",BeanNameUtil.toGetterName(name, false));
			Assert.assertEquals("for " + name,"isGetter",BeanNameUtil.toGetterName(name, true));
		}
		for(String name:new String[]{"setter"}){
			Assert.assertEquals("for " + name,"getSetter",BeanNameUtil.toGetterName(name, false));
			Assert.assertEquals("for " + name,"isSetter",BeanNameUtil.toGetterName(name, true));
		}
		for(String name:new String[]{"get"}){
			Assert.assertEquals("for " + name,"getGet",BeanNameUtil.toGetterName(name, false));
			Assert.assertEquals("for " + name,"isGet",BeanNameUtil.toGetterName(name, true));
		}
		for(String name:new String[]{"set"}){
			Assert.assertEquals("for " + name,"getSet",BeanNameUtil.toGetterName(name, false));
			Assert.assertEquals("for " + name,"isSet",BeanNameUtil.toGetterName(name, true));
		}
		
	}
	
	@Test
	public void toSetterName(){
		for(String name:new String[]{"getFoo", "foo", "Foo", "setFoo","isFoo"}){
			Assert.assertEquals("for " + name,"setFoo",BeanNameUtil.toSetterName(name));
		}
		Assert.assertEquals("setSetter",BeanNameUtil.toSetterName("setter"));
		Assert.assertEquals("setSet",BeanNameUtil.toSetterName("set"));
	}
	
	
	@Test
	public void extractIndexedKeyType(){
		Assert.assertEquals("String",BeanNameUtil.extractIndexedKeyType("com.X<String,Integer>"));
		Assert.assertEquals("Foo<Bar>",BeanNameUtil.extractIndexedKeyType("com.X<Foo<Bar>,String>"));
		Assert.assertEquals("java.lang.Object",BeanNameUtil.extractIndexedKeyType("com.X"));
		Assert.assertEquals(null,BeanNameUtil.extractIndexedKeyType("com.X<String>"));
	}
	
	@Test
	public void extractIndexedValueType(){
		Assert.assertEquals("String",BeanNameUtil.extractIndexedValueType("com.X<String>"));
		Assert.assertEquals("Integer",BeanNameUtil.extractIndexedValueType("com.X<String,Integer>"));
		Assert.assertEquals("Foo<Bar>",BeanNameUtil.extractIndexedValueType("com.X<String,Foo<Bar>>"));
		//Assert.assertEquals("String",BeanNameUtil.extractIndexedValueType("com.X<Foo<Bar>,String>"));
		
		Assert.assertEquals("Foo<Alice,Bob>",BeanNameUtil.extractIndexedValueType("com.X<String,Foo<Alice,Bob>>"));
		
		Assert.assertEquals("java.lang.Object",BeanNameUtil.extractIndexedValueType("com.X"));
	}
	
}

package org.codemucker.jmatch;

import java.util.List;

import org.codemucker.jmatch.DefaultDescription;
import org.codemucker.jmatch.Description;
import org.codemucker.jmatch.SelfDescribing;
import org.junit.Test;

import com.google.common.collect.Lists;

public class DefaultDescriptionTest {

	private static final String NL = System.getProperty("line.separator");
	private static final String INDENT = "    ";
	
	@Test
	public void testText(){
		assertEquals(desc().text("abc"),"abc");
	}
	
	@Test
	public void testText1Arg(){
		assertEquals(desc().text("abc%s","d"),"abcd");
	}
	
	@Test
	public void testText2Args(){
		assertEquals(desc().text("abc%s%s","d","e"),"abcde");
	}
	
	@Test
	public void testText3Args(){
		assertEquals(desc().text("abc%s%s%s","d","e", "f"),"abcdef");
	}
	
	@Test
	public void testText4Args(){
		assertEquals(desc().text("abc%s%s%s%s","d","e","f","g"),"abcdefg");
	}
	
	@Test
	public void testVal(){
		assertEquals(desc().value("label","value"),"label:value");
	}

	@Test
	public void testValWithSelfDesc(){
		Object myVal = new SelfDescribing() {
			@Override
			public void describeTo(Description desc) {
				desc.text("myValue1");
				desc.text("myValue2");
			}
		};
		assertEquals(
			desc().value("label",myVal),
			expect()
				.append("label:")
				.append(NL).append(INDENT).append("myValue1")
				.append(NL).append(INDENT).append("myValue2")
		);
	}
	
	@Test
	public void testLabelValWithSelfDesc(){
		Object myVal = new SelfDescribing() {		
			@Override
			public void describeTo(Description desc) {
				desc.text("mySubValue");
				desc.value("mySubLabel","mySubValue");
				
			}
		};
		assertEquals(
			desc().value("myLabel", myVal),
			expect()
				.append("myLabel:")
				.append(NL).append(INDENT).append("mySubValue")
				.append(NL).append(INDENT).append("mySubLabel:mySubValue")
				
		);
	}

	@Test
	public void testValues(){
		List<String> values = Lists.newArrayList("child1","child2","child3");
		assertEquals(
			desc()
				.text("abc")
				.values("label",values)
				.text("123"),
			expect()
				.append("abc")
				.append(NL).append("label:")
				.append(NL).append(INDENT).append("child1,")
				.append(NL).append(INDENT).append("child2,")
				.append(NL).append(INDENT).append("child3")
				.append(NL).append("123")
		);
	}
	
	@Test
	public void testSubValues(){
		Object child = new SelfDescribing() {
			
			@Override
			public void describeTo(Description desc) {
				desc.text("subValObject");
				List<String> childChildren = Lists.newArrayList("subVal1","subVal2","subVal3");
				
				desc.values("subValues", childChildren);
			}
		};
		List<Object> children = Lists.newArrayList("val1",child,"val3");
		
		assertEquals(
			desc()
				.text("abc")
				.values("values",children)
				.text("123"),
			expect()
				.append("abc")
				.append(NL).append("values:")
				.append(NL).append(INDENT).append("val1,")
				
				.append(NL).append(INDENT).append("subValObject")
				.append(NL).append(INDENT).append("subValues:")
				.append(NL).append(INDENT).append(INDENT).append("subVal1,")
				.append(NL).append(INDENT).append(INDENT).append("subVal2,")
				.append(NL).append(INDENT).append(INDENT).append("subVal3")
				.append(NL).append(INDENT).append(",")
				.append(NL).append(INDENT).append("val3")
				.append(NL).append("123")
		);
	}
	
	
	
	@Test
	public void testLabelVal(){
		assertEquals(desc().value("myLabel","myValue"),"myLabel:myValue");
	}
	
	private void assertEquals(Description d,StringBuilder expect){
		assertEquals(d, expect.toString());
	}
	
	private void assertEquals(Description d,String expect){
		String actual = d.toString();
		if(!expect.equals(actual)){
			throw new AssertionError(String.format("expected %n '%s'%n but was %n'%s'%n", expect,actual));
		}
	}
	
	private DefaultDescription desc(){
		return new DefaultDescription();
	}
	
	private StringBuilder expect(){
		return new StringBuilder();
	}
}

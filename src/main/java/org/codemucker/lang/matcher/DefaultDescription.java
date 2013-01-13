package org.codemucker.lang.matcher;

import java.util.Iterator;
import java.util.Stack;

public class DefaultDescription implements Description {

	private final StringBuilder sb = new StringBuilder();
	private boolean requireNewLine = false;
	private static final String NL = System.clearProperty("line.separator");
	private static final String INDENT = "    ";
	
	private String currentIndent;
	private Stack<String> indents = new Stack<String>();
	
	public DefaultDescription() {
		this("");
	}

	private DefaultDescription(String indent) {
		this.currentIndent = indent;
	}

	@Override
	public Description text(String s) {
		appendIndent();
		appendLine(s);
		return this;
	}

	@Override
	public Description text(String s, Object arg1) {
		appendIndent();
		appendLine(String.format(s, arg1));
		return this;
	}

	@Override
	public Description text(String s, Object arg1, Object arg2) {
		appendIndent();
		appendLine(String.format(s, arg1, arg2));
		return this;
	}

	@Override
	public Description text(String s, Object arg1, Object arg2, Object arg3) {
		appendIndent();
		appendLine(String.format(s, arg1, arg2, arg3));
		return this;
	}

	@Override
	public Description text(String s, Object... args) {
		appendIndent();
		appendLine(String.format(s, args));
		return this;
	}

	@Override
	public Description values(String label,Iterable<?> children) {
		appendIndent();
		append(label);
		appendLine(":");
		
		if( children == null){
			appendIndent();
			appendVal("null");
			return this;
		} else {
			increaseIndent();
			for(Iterator<?> iter = children.iterator();iter.hasNext();){
				Object child = iter.next();
				if(!(child instanceof SelfDescribing)){
					appendIndent();	
				}
				appendVal(child);
				if( iter.hasNext()){
					if(child instanceof SelfDescribing){
						appendIndent();	
					}
					append(",");
				}
				appendLine();
			}
			decreaseIndent();
		}
		return this;
	}
	
	@Override
	public Description child(Object child) {
		increaseIndent();
		appendIndent();
		appendVal(child);
		appendLine();
		decreaseIndent();
		return this;
	}
	
	@Override
	public Description value(String label, Object value) {
		appendIndent();
		append(label);
		append(":");
		if( value instanceof SelfDescribing){
			increaseIndent();
			appendVal(value);
			decreaseIndent();
		} else {
			appendVal(value);	
		}
		appendLine();
		return this;
	}
	
	private void appendIndent(){
		append(currentIndent);
	}
	
	private void appendLine(){
		requireNewLine = true;
	}
	
	//append the given text, setting the neext append to first prepend a newline
	private void appendLine(String s){
		append(s);
		requireNewLine = true;
	}
	
	
	private void newLineIfRequired(){
		if(requireNewLine){
			requireNewLine = false;
			sb.append(NL);
		}
	}
	
	private void appendVal(Object val){
		if( val instanceof SelfDescribing){
			appendLine();
			((SelfDescribing)val).describeTo(this);
		} else {
			append(val==null?"null":val.toString());
		}
	}
	
	//append the given text, first printing a newline if required by a previous println
	private void append(String s){
		newLineIfRequired();
		sb.append(s);
	}
	
	private void increaseIndent() {
		indents.push(currentIndent);
		currentIndent = currentIndent + INDENT;
	}

	private void decreaseIndent() {
		currentIndent = indents.pop();
	}

	@Override
	public boolean isNull() {
		return false;
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
}

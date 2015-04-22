package org.codemucker.lang.event;


public interface ListenerRegistry<T> {

	void add(T listener);	
	void addAll(Iterable<T> listeners);
	void addAll(T[] listeners);

	
	void remove(T listener);
	void removeAll(Iterable<T> listeners);
	void removeAll(T[] listeners);

}

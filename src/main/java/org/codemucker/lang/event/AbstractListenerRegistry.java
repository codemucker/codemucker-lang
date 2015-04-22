package org.codemucker.lang.event;

import org.codemucker.lang.annotation.ThreadSafe;

/**
 * Collect a bunch of listeners
 * 
 * <p>Usage:
 * <pre>
 * 
 * MySubClass extends AbstractListenerRegistry&lt;MyListener&gt; {....
 * 
 * public void onMyEvent(MyEvent evt) {
 *		MyListener[] safeListeners = getListeners();//ensures any listeners add/removed elsewhere don't mess with our loop
 *		for (MyListener listener : safeListeners) {
 *			try {
 *				listener.onMyEvent(evt);
 *			} catch (Exception e) {
 *				onListenerError(listener, e);//ensure a failing listener doesn't stuff it up for the others
 *			}
 *		}
 *	}
 * </pre>
 * </p>
 * @param <L> the listener type
 */
@ThreadSafe
public abstract class AbstractListenerRegistry<L extends Object> implements ListenerRegistry<L> {
	
	private L[] listeners;

	private final Object lock = new Object();

	public AbstractListenerRegistry() {
		listeners = newArray(0);
	}
	
	public AbstractListenerRegistry(L listener) {
		listeners = newArray(0);
		add(listener);
	}
	
	public AbstractListenerRegistry(L[] listeners) {
		this.listeners = newArray(0);
		addAll(listeners);
	}
	
	@Override
	public void addAll(L[] listeners){
		if(listeners == null){
			return;
		}
		for(L listener:listeners){
			add(listener);
		}
	}
	
	@Override
	public void addAll(Iterable<L> listeners){
		if(listeners == null){
			return;
		}
		for(L listener:listeners){
			add(listener);
		}
	}

	@Override
	public void add(L listener){
		if(listener == null){
			return;
		}
		synchronized (lock) {
			for(L l:listeners){
				//only add the listener once
				if(l == listener){
					return;
				}
			}
			L[] newListeners = newArray(listeners.length + 1); 
			System.arraycopy(listeners, 0, newListeners, 0, listeners.length);
			newListeners[newListeners.length-1] = listener;
			this.listeners = newListeners;
		}
	}

	@Override
	public void removeAll(L[] listeners){
		if(listeners == null){
			return;
		}
		for(L listener:listeners){
			remove(listener);
		}
	}

	
	@Override
	public void removeAll(Iterable<L> listeners){
		if(listeners == null){
			return;
		}
		for(L listener:listeners){
			remove(listener);
		}
	}

	@Override
	public void remove(L listener){
		if(listener == null){
			return;
		}
		synchronized (lock) {
			for(int i = 0; i < listeners.length;i++){
				if(listeners[i] == listener){
					L[] newListeners = newArray(listeners.length-1);
					//copy listeners before
					System.arraycopy(listeners, 0, newListeners, 0, i);
					//copy listeners after
					if(i<listeners.length-1){//not last one
						System.arraycopy(listener, i+1, newListeners, i, listeners.length-i-1);
					}
					this.listeners = newListeners;
					return;
				}
			}

		}
	}
	
	public void removeAll(){
		synchronized (lock) {
			listeners = newArray(0);
		}
	}
	
	protected abstract L[] newArray(int length);

	/**
	 * Called when a listener throws an error. Default implementation just logs the error
	 * @param listener
	 * @param e
	 */
	protected void onListenerError(L listener, Exception e){
		e.printStackTrace();
	}
	
	protected L[] getListeners(){
		return listeners;
	}
}
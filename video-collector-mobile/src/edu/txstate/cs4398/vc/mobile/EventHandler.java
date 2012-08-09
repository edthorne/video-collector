package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
/**
 * Class that holds all listeners and then notifies them of changes.
 * @author Rudolph Newball
 *
 */
public class EventHandler {
	private List<Listener> listeners = new ArrayList<Listener>();
	
	/**
	 * Adds listener to handler pool
	 * @param listener to be added to the handler pool
	 */
	public synchronized void addEventListener(Listener listener){
		listeners.add(listener);
	}
	/**
	 * Removes the listener passed in
	 * @param listener listener to be removed
	 */
	public synchronized void removeEventListener(Listener listener){
		listeners.remove(listener);
	}
	/**
	 * Notifies all listeners of the task completed
	 * @param task the task completed thats sent to listeners
	 */
	public synchronized void notifyEvent(TaskEvent task){
		Iterator<Listener> i = listeners.iterator();
		while(i.hasNext())	{
			((Listener) i.next()).onEvent(task);
		}
	}
		

}

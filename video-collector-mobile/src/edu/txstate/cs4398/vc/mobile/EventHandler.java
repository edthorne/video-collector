package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

public class EventHandler {
	private List<Listener> listeners = new ArrayList<Listener>();
	public synchronized void addEventListener(Listener listener){
		Log.i("Interfaces", "Adding listener");
		listeners.add(listener);
	}
	public synchronized void removeEventListener(Listener listener){
		listeners.remove(listener);
	}
	
	public synchronized void notifyEvent(Task task){
		Iterator<Listener> i = listeners.iterator();
		while(i.hasNext())	{
			((Listener) i.next()).onEvent(task);
		}
	}
		

}

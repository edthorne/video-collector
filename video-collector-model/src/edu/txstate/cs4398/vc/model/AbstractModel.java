package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract root class of Model hierarchy - provides basic notification behavior
 * 
 * @author John Hunt, Planet Java
 */
public abstract class AbstractModel implements Model {

	/**
	 * This list of listeners listening for changes to the model. 
	 */
	private ArrayList<ModelListener> listeners = new ArrayList<ModelListener>();

	/**
	 * Method that is called by subclasses of AbstractModel when they want to
	 * notify other classes of changes to themselves.
	 * 
	 * @param event
	 *            the <code>ModelEvent</code> triggered by the change
	 */
	@SuppressWarnings("unchecked")
	public void notifyChanged(ModelEvent event) {
		// gotta clone the list or face concurrent modification issues
		List<ModelListener> list = (List<ModelListener>) listeners.clone();
		Iterator<ModelListener> it = list.iterator();
		while (it.hasNext()) {
			ModelListener ml = it.next();
			ml.modelChanged(event);
		}
	}

	/**
	 * Adds a listener to the model.
	 * 
	 * @param listener the <code>ModelListener</code> to be added
	 */
	public void addModelListener(ModelListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener from the model.
	 * 
	 * @param listener the <code>ModelListener</code> to be removed
	 */
	public void removeModelListener(ModelListener listener) {
		listeners.remove(listener);
	}
}
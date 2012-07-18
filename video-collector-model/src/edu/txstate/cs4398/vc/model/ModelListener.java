package edu.txstate.cs4398.vc.model;

/**
 * Responds to <code>ModelEvent</code> objects when delivered.
 * 
 * @author John Hunt, Planet Java
 */
public interface ModelListener {
	/**
	 * This method is invoked in response to a <code>ModelEvent</code> being
	 * dispatched.
	 * 
	 * @param event
	 *            the event being dispatched
	 */
	public void modelChanged(ModelEvent event);
}
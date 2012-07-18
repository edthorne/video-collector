package edu.txstate.cs4398.vc.model;

/**
 * This interface must be implemented by all class that wish to play the Model
 * role within the MVC framework.
 * <p>
 * The only method specified by the interface is the
 * <code>notifyChanged()</code> method.
 * 
 * @author John Hunt, Planet Java
 */
public interface Model {
	/**
	 * Method that is called by subclasses of AbstractModel when they want to
	 * notify other classes of changes to themselves.
	 * 
	 * @param event
	 *            the <code>ModelEvent</code> triggered by the change
	 */
	public void notifyChanged(ModelEvent event);

}

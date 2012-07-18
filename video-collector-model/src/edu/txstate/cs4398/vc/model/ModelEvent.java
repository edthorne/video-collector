package edu.txstate.cs4398.vc.model;

import java.awt.event.ActionEvent;

/**
 * Used to notify interested objects of changes in the state of a model.
 * 
 * @author John Hunt, Planet Java
 */
public class ModelEvent extends ActionEvent {

	/**
	 * Creates a new <code>ModelEvent</code> with the identified values.
	 * 
	 * @param source
	 *            the object that originated the event
	 * @param id
	 *            an integer that identifies the event
	 * @param command
	 *            a string that may specify a command (possibly one of several)
	 *            associated with the event
	 */
	public ModelEvent(Object source, int id, String command) {
		super(source, id, command);
		// TODO Auto-generated constructor stub
	}

}

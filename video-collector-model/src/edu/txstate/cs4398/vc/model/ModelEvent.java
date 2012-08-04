package edu.txstate.cs4398.vc.model;

/**
 * Used to notify interested objects of changes in the state of a model.
 * 
 * @author John Hunt, Planet Java
 */
public class ModelEvent  {

	protected int id;
	protected transient Object  source;
	String actionCommand;

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
		this.source = source;
		this.id = id;
		this.actionCommand = command;

	}

	public int getID() {
		return id;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public Object getSource() {
		return source;
	}


}

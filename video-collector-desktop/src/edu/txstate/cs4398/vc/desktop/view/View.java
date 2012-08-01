/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.view;

import edu.txstate.cs4398.vc.desktop.controller.Controller;
import edu.txstate.cs4398.vc.model.Model;

/**
 * A generic interface that represents the view used in the MVC framework.
 * 
 * @author Ed
 */
public interface View {
	/**
	 * Returns the controller associated with the view.
	 * 
	 * @return the controller associated with the view
	 */
	public Controller getController();

	/**
	 * Sets the controller associated with the view.
	 * 
	 * @param controller
	 *            the controller associated with the view
	 */
	public void setController(Controller controller);

	/**
	 * Returns the model associated with the view.
	 * 
	 * @return the model associated with the view
	 */
	public Model getModel();

	/**
	 * Sets the model associated with the view.
	 * 
	 * @param model
	 *            the model associated with the view
	 */
	public void setModel(Model model);
}
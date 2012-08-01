/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.controller;

import edu.txstate.cs4398.vc.desktop.view.View;
import edu.txstate.cs4398.vc.model.Model;

/**
 * Represents the Controller in a Model-View-Controller architecture.
 * 
 * @author Ed
 */
public interface Controller {
	/**
	 * Returns the model associated with the controller
	 * 
	 * @return the model associated with the controller
	 */
	public Model getModel();

	/**
	 * Sets the model associated with the controller
	 * 
	 * @param model
	 *            the model to associate
	 */
	public void setModel(Model model);

	/**
	 * Returns the view associated with the controller.
	 * 
	 * @return the view associated with the controller
	 */
	public View getView();

	/**
	 * Sets the view associated with the controller
	 * 
	 * @param view
	 *            the view to associate
	 */
	public void setView(View view);
}

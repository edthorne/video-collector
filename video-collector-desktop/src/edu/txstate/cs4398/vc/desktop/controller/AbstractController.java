/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.controller;

import edu.txstate.cs4398.vc.desktop.view.View;
import edu.txstate.cs4398.vc.model.Model;

/**
 * Abstract base class that manages storage and retrieval of the model and view
 * associated with the controller.
 * 
 * @author Ed
 */
public abstract class AbstractController implements Controller {
	/**
	 * The model.
	 */
	private Model model;
	/**
	 * The view.
	 */
	private View view;

	/**
	 * Returns the model for the controller.
	 * 
	 * @return the model for the controller
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Returns the view for the controller.
	 * 
	 * @return the view for the controller
	 */
	public View getView() {
		return view;
	}

	/**
	 * Sets the model for the controller.
	 * 
	 * @param model
	 *            the model for the controller
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * Sets the view for the controller.
	 * 
	 * @param view
	 *            the view for the controller
	 */
	public void setView(View view) {
		this.view = view;
	}
}

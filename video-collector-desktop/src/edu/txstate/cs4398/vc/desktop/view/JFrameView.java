/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.view;

import javax.swing.JFrame;

import edu.txstate.cs4398.vc.desktop.controller.Controller;
import edu.txstate.cs4398.vc.model.AbstractModel;
import edu.txstate.cs4398.vc.model.Model;
import edu.txstate.cs4398.vc.model.ModelListener;

/**
 * The JFrameView class is the root class of the view class hierarchy for top
 * level (swing) frames. It allows a controller and a model to be registered and
 * can register itself with a model as an observer of that model.
 * <p>
 * It extends the JFrame class.
 * <p>
 * It requires the implementation of the
 * <code>modelChanged(ModelEvent event);</code> method in order that it can work
 * with the notification mechanism in Java.
 * 
 * @author John Hunt, Planet Java
 * @author Ed
 */
public abstract class JFrameView extends JFrame implements View, ModelListener {
	/**
	 * Serializable classes need a unique version id
	 */
	private static final long serialVersionUID = 6022212020070780911L;

	/**
	 * The model that corresponds to this view.
	 */
	private Model model;
	/**
	 * The controller that corresponds to this view.
	 */
	private Controller controller;

	/**
	 * Constructs a new JFrameView with the given model and controller.
	 * 
	 * @param model
	 *            the associated model
	 * @param controller
	 *            the associated controller
	 */
	public JFrameView(Model model, Controller controller) {
		setModel(model);
		setController(controller);
	}

	/**
	 * Registers the view as a listener to the model.
	 */
	public void registerWithModel() {
		if (model != null) {
			((AbstractModel) model).addModelListener(this);
		}
	}

	/**
	 * Returns the controller associated with this view.
	 * 
	 * @return the controller associated with this view
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Sets the controller to be associated with this view.
	 * 
	 * @param controller
	 *            the controller to associate
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Returns the model associated with this view.
	 * 
	 * @return the model associated with this view
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Sets the model to be associated with this view.
	 * 
	 * @param model
	 *            the model to associate
	 */
	public void setModel(Model model) {
		this.model = model;
		registerWithModel();
	}
}
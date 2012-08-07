/**
 * 
 */
package edu.txstate.cs4398.vc.desktop;

import javax.swing.UIManager;

import edu.txstate.cs4398.vc.desktop.controller.CollectorController;

/**
 * @author Ed
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// use the native OS look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// eat the exceptions since getSystemLookAndFeelClassName should
			// always be a valid PLAF choice
		}

		// start the application by creating a controller
		CollectorController.getInstance();
	}
}

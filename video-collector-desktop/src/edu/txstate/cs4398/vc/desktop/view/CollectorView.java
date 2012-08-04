package edu.txstate.cs4398.vc.desktop.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.txstate.cs4398.vc.desktop.controller.CollectorController;
import edu.txstate.cs4398.vc.desktop.model.CollectorModel;
import edu.txstate.cs4398.vc.model.ModelEvent;

/**
 * The primary appication view.
 * 
 * @author Ed
 */
@SuppressWarnings("serial")
public class CollectorView extends JFrameView {

	private Action newCollectionAction;
	private Action newVideoAction;
	private Action openAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action servicesAction;
	private Action exitAction;
	private Action ipAddrAction;

	private JCheckBoxMenuItem remoteServices;

	public CollectorView(CollectorModel model, CollectorController controller) {
		super(model, controller);
		// close the application when the frame closes
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// create a window listener to catch closing and prompt to save
		this.addWindowListener(new CollectorViewWindowAdapter());
		// set the window title
		setWindowTitle();

		// create actions
		newCollectionAction = new NewCollectionAction("Collection",
				"Creates a new collection", KeyEvent.VK_C);
		newVideoAction = new NewVideoAction("Video", "Creates a new video",
				KeyEvent.VK_V);
		openAction = new OpenAction("Open...", "Open a collection file",
				KeyEvent.VK_O);
		saveAction = new SaveAction("Save", "Saves a collection file",
				KeyEvent.VK_S);
		saveAsAction = new SaveAsAction("Save As...",
				"Saves a collection file", KeyEvent.VK_A);
		servicesAction = new ServicesAction("Remote Services",
				"Enable/disable remote services", KeyEvent.VK_R);
		exitAction = new ExitAction("Exit", "Exits the application",
				KeyEvent.VK_X);
		ipAddrAction = new IPAddrAction("IP Address",
				"Shows the IP address of this computer", KeyEvent.VK_I);

		// create the menu bar
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenu subMenu = new JMenu("New");
		JMenuItem item = new JMenuItem(newCollectionAction);
		subMenu.add(item);
		item = new JMenuItem(newVideoAction);
		subMenu.add(item);
		menu.add(subMenu);
		menu.addSeparator();
		item = new JMenuItem(openAction);
		menu.add(item);
		item = new JMenuItem(saveAction);
		menu.add(item);
		item = new JMenuItem(saveAsAction);
		menu.add(item);
		menu.addSeparator();
		remoteServices = new JCheckBoxMenuItem(servicesAction);
		menu.add(remoteServices);
		menu.addSeparator();
		item = new JMenuItem(exitAction);
		menu.add(item);
		menubar.add(menu);
		menu = new JMenu("Help");
		item = new JMenuItem(ipAddrAction);
		menu.add(item);
		menubar.add(menu);
		this.setJMenuBar(menubar);

		// add the
		this.setPreferredSize(new Dimension(800, 600));

		// size everything accordingly
		pack();

		// display centered in the screen now that we know the width
		this.setLocationRelativeTo(null);
	}

	@Override
	public void modelChanged(ModelEvent event) {
		// we're listening to the collector model for property changes
		setWindowTitle();
	}

	@Override
	public CollectorController getController() {
		return (CollectorController) super.getController();
	}

	@Override
	public CollectorModel getModel() {
		return (CollectorModel) super.getModel();
	}

	private void setWindowTitle() {
		StringBuilder builder = new StringBuilder();
		builder.append("Video Collector");
		CollectorModel model = getModel();
		builder.append(" - ");
		if (model.isDirty()) {
			builder.append("*");
		}
		if (model.getFile() != null) {
			builder.append(model.getFile().getAbsolutePath());
		} else {
			builder.append("Untitled");
		}
		setTitle(builder.toString());
	}

	class CollectorViewWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			// if the model is dirty, prompt to save
			if (getModel().isDirty()) {
				int answer = JOptionPane
						.showConfirmDialog(
								CollectorView.this,
								"The collection has been modified. Do you wish to save the changes before closing?",
								"Unsaved Changes", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (answer == JOptionPane.YES_OPTION) {
					if (getModel().getFile() != null) {
						getController().save();
					} else {
						getController().saveAs();
					}
				}
			}
		}
	}

	class NewCollectionAction extends AbstractAction {
		public NewCollectionAction(String text, String description,
				Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getController().newCollection();
		}
	}

	class NewVideoAction extends AbstractAction {
		public NewVideoAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getController().newVideo();
		}
	}

	class OpenAction extends AbstractAction {
		public OpenAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getController().open();
		}
	}

	class SaveAction extends AbstractAction {
		public SaveAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getController().save();
		}
	}

	class SaveAsAction extends AbstractAction {
		public SaveAsAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getController().saveAs();
		}
	}

	class ServicesAction extends AbstractAction {
		public ServicesAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			// set the remote service state to match check box state
			getController().setRemoteServiceState(remoteServices.isSelected());
		}
	}

	class ExitAction extends AbstractAction {
		public ExitAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			dispatchEvent(new WindowEvent(CollectorView.this,
					WindowEvent.WINDOW_CLOSING));
		}
	}

	class IPAddrAction extends AbstractAction {
		public IPAddrAction(String text, String description, Integer mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, description);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			JOptionPane.showMessageDialog(CollectorView.this,
					"This computer's IP address is "
							+ getModel().getIPAddress(), "IP Address",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

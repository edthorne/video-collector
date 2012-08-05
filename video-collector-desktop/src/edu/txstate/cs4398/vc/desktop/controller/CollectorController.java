package edu.txstate.cs4398.vc.desktop.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Endpoint;

import edu.txstate.cs4398.vc.desktop.model.CollectorModel;
import edu.txstate.cs4398.vc.desktop.services.MobileServicesImpl;
import edu.txstate.cs4398.vc.desktop.utils.DiscoveryListener;
import edu.txstate.cs4398.vc.desktop.utils.NetworkUtils;
import edu.txstate.cs4398.vc.desktop.view.CollectorView;

/**
 * The primary application controller for the desktop application.
 * 
 * @author Ed
 */
public class CollectorController extends AbstractController {
	/**
	 * A shared instance of the file chooser used to select a file to open or
	 * save. Since there's one instance the controller essentially remembers the
	 * last file selected.
	 */
	private JFileChooser fileChooser;

	private Endpoint wsEndpoint;
	private String wsURI = null;
	private DiscoveryListener discoveryListener;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	/**
	 * Creates a new application controller.
	 */
	public CollectorController() {
		// make sure we can use JAXB for reading/writing application data
		try {
			JAXBContext context = JAXBContext.newInstance(CollectorModel.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			unmarshaller = context.createUnmarshaller();
		} catch (PropertyException pe) {
			// thrown if property can't be set
			pe.printStackTrace();
		} catch (JAXBException jaxbe) {
			// thrown if the context or marshallers can't be created.
			// if this happens we can't run!
			System.err.println("Unable to initialize JAXB, shutting down!");
			jaxbe.printStackTrace();
			System.exit(1);
		}

		// create our file chooser
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Video Collection Database", "vcd");
		fileChooser.setFileFilter(filter);

		// build the web service URL
		try {
			wsURI = new URI("http", null, NetworkUtils.getIPAddress(), 8796,
					"/MobileServices", null, null).toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// set the application model
		setModel(new CollectorModel());

		// create the view and make it visible
		setView(new CollectorView(getModel(), this));
		getView().setVisible(true);
	}

	@Override
	public CollectorModel getModel() {
		return (CollectorModel) super.getModel();
	}

	@Override
	public CollectorView getView() {
		return (CollectorView) super.getView();
	}

	/**
	 * Creates a new collection. If the current collection is dirty the user is
	 * prompted to save it.
	 */
	public void newCollection() {
		// if the model is dirty offer the user a chance to save it
		if (!checkDirtySave()) {
			return; // user canceled the dialog
		}

		// TODO: if the services are running stop them!

		// create the new model
		setModel(new CollectorModel()); // controller
		getView().setModel(getModel()); // view
	}

	/**
	 * Causes a new window to be presented
	 */
	public void newVideo() {
		// TODO Auto-generated method stub

	}

	/**
	 * Loads the application model from a file selected by the user.
	 */
	public void open() {
		// if the model is dirty offer the user a chance to save it
		if (!checkDirtySave()) {
			return; // user canceled the dialog
		}

		// TODO: if the services are running stop them!

		// use fileChooser to select our file
		int choice = fileChooser.showOpenDialog(getView());
		switch (choice) {
		case JFileChooser.APPROVE_OPTION:
			// get file from chooser
			File file = fileChooser.getSelectedFile();
			CollectorModel model;
			try {
				// unmarshal the collector model from the file
				model = (CollectorModel) unmarshaller.unmarshal(file);
			} catch (JAXBException jaxbe) {
				// unexpected errors, validation, or binding problems
				JOptionPane.showMessageDialog(getView(), jaxbe.getMessage(),
						"Error opening file", JOptionPane.ERROR_MESSAGE);
				jaxbe.printStackTrace();
				return;
			}
			// record where we opened the file from in the model
			model.setFile(file);
			// set the new model
			setModel(model); // controller
			getView().setModel(model); // view

			break;
		case JFileChooser.CANCEL_OPTION:
			// user canceled or closed without selection
			break;
		}
	}

	/**
	 * Saves the collection. If no file is selected, the user is presented a
	 * file selection dialog to pick the save file.
	 */
	public void save() {
		// if we don't have a file, ask the user to pick one
		if (getModel().getFile() == null) {
			saveAs();
		} else {
			try {
				marshaller.marshal(getModel(), getModel().getFile());
			} catch (JAXBException jaxbe) {
				// unexpected errors, validation, or binding problems
				JOptionPane.showMessageDialog(getView(), jaxbe.getMessage(),
						"Error opening file", JOptionPane.ERROR_MESSAGE);
				jaxbe.printStackTrace();
				return;
			}
		}
	}

	/**
	 * Prompts the user to select a file to save the collection to and then
	 * saves the collection.
	 */
	public void saveAs() {
		// have the user select the file
		int choice = fileChooser.showSaveDialog(getView());
		switch (choice) {
		case JFileChooser.APPROVE_OPTION:
			// get file from chooser
			getModel().setFile(fileChooser.getSelectedFile());
			save();
			break;
		case JFileChooser.CANCEL_OPTION:
			// user canceled or closed without selection
			break;
		}
	}

	public void setRemoteServiceState(boolean enabled) {
		if (enabled) {
			startListener();
			startWebService();
		} else {
			stopListener();
			stopWebService();
		}
	}

	/**
	 * Checks if the model is dirty and offers to save it. If the user cancels
	 * the dialog, this method returns false as a signal to the caller to not
	 * proceed.
	 * 
	 * @return true if the user saves or skips save, false if they cancel
	 */
	private boolean checkDirtySave() {
		// if the existing model is dirty we should prompt to save it
		if (getModel().isDirty()) {
			int choice = JOptionPane.showConfirmDialog(getView(),
					"Do you want to save your recent changes?",
					"Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			switch (choice) {
			case JOptionPane.YES_OPTION:
				if (getModel().getFile() != null) {
					save();
				} else {
					saveAs();
				}
				break;
			case JOptionPane.NO_OPTION:
				// no operation required
				break;
			case JOptionPane.CANCEL_OPTION:
				// user elected to cancel, stop create process
				return false;
			}
		}
		// clean model, or user skipped save
		return true;
	}

	private void startListener() {
		// create the discovery listener
		try {
			discoveryListener = new DiscoveryListener();
		} catch (IOException ioe) {
			System.err.println("Unable to create discovery listener.");
			ioe.printStackTrace();
			return;
		}
		discoveryListener.setServiceAddress(wsURI);
		discoveryListener.start();
	}

	private void startWebService() {
		// create the web service endpoint
		wsEndpoint = Endpoint.create(new MobileServicesImpl(getModel()));
		System.out.println("Publishing webservice at " + wsURI);
		wsEndpoint.publish(wsURI);
	}

	private void stopListener() {
		discoveryListener.setListening(false);
	}

	private void stopWebService() {
		System.out.println("Stopping webservice at " + wsURI);
		wsEndpoint.stop();
	}
}

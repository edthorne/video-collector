package edu.txstate.cs4398.vc.desktop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

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
import edu.txstate.cs4398.vc.desktop.services.VideoLookupService;
import edu.txstate.cs4398.vc.desktop.utils.DiscoveryListener;
import edu.txstate.cs4398.vc.desktop.utils.NetworkUtils;
import edu.txstate.cs4398.vc.desktop.view.CollectorView;
import edu.txstate.cs4398.vc.model.Video;

/**
 * The primary application controller for the desktop application.
 * 
 * @author Ed
 */
public class CollectorController extends AbstractController {

	private static final String TOMATO_API_TOKEN = "tomato_api_token";

	private static final String UPC_API_TOKEN = "upc_api_token";

	/**
	 * The private instance for the singleton interface.
	 */
	private static CollectorController instance;

	/**
	 * @return the singleton instance of the application controller
	 */
	public static synchronized CollectorController getInstance() {
		if (instance == null) {
			instance = new CollectorController();
		}
		return instance;
	}

	/**
	 * A shared instance of the file chooser used to select a file to open or
	 * save. Since there's one instance the controller essentially remembers the
	 * last file selected.
	 */
	private JFileChooser fileChooser;

	/**
	 * An instance of the video lookup service for use by the application.
	 */
	private VideoLookupService videoLookup;

	private Endpoint wsEndpoint;
	private String wsURI = null;
	private DiscoveryListener discoveryListener;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	/**
	 * Creates a new application controller.
	 */
	private CollectorController() {
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
		fileChooser.setAcceptAllFileFilterUsed(false);

		// build the web service URL
		try {
			wsURI = new URI("http", null, NetworkUtils.getIPAddress(), 8796,
					"/MobileServices", null, null).toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// get the video lookup service properties
		Properties vlsProps = getVLSConfig();

		// set up the video lookup service
		String upcToken = vlsProps.getProperty(UPC_API_TOKEN);
		String tomatoesToken = vlsProps.getProperty(TOMATO_API_TOKEN);
		videoLookup = new VideoLookupService(upcToken, tomatoesToken);

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

	/**
	 * @return a reference to the video lookup service
	 */
	public VideoLookupService getVideoLookupService() {
		return videoLookup;
	}

	@Override
	public CollectorView getView() {
		return (CollectorView) super.getView();
	}

	/**
	 * Edits a video displayed in the UI.
	 */
	public void edit(Video video) {
		new VideoController(video, getModel().getCollection());
	}
	
	/**
	 * Deletes the video from the collection
	 */
	public void delete(Video video) {
		getModel().getCollection().removeVideo(video);
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
	 * Causes a new video editor window to be presented.
	 */
	public void newVideo() {
		new VideoController(null, getModel().getCollection());
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
			getModel().setDirty(false);
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
			File selectedFile = fileChooser.getSelectedFile();
			// make sure it complies with the vcd file filter
			if (!fileChooser.accept(selectedFile)) {
				// file doesn't have .vcd extension
				selectedFile = new File(selectedFile.getPath() + ".vcd");
			}
			getModel().setFile(selectedFile);
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

	private void createVLSCfgTemplate(File vlsCfg) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(vlsCfg));
			writer.println("# In order to use the video lookup services you will need to obtain keys for two");
			writer.println("# web service APIs.  See http://searchupc.com/api/ for information on how to");
			writer.println("# create your account to obtain a key for the following property:");
			writer.println("upc_api_token=");
			writer.println("# See http://developer.rottentomatoes.com/ for information on how to create");
			writer.println("# your account to obtain a key for the following property:");
			writer.println("tomato_api_token=");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"There was a problem creating the VLS configuration file ("
							+ vlsCfg.getAbsolutePath() + ")",
					"Error Creating Configuration", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Load the Video Lookup Service config. If the configuration does not exist
	 * the user will be prompted to create it.
	 * 
	 * @return a properties object loaded with the VLS configuration
	 */
	private Properties getVLSConfig() {
		final String filename = "vls.cfg";
		Properties vlsProps = new Properties();
		File vlsPropsFile = new File(System.getProperty("user.home"), filename);
		FileReader reader = null;
		try {
			reader = new FileReader(vlsPropsFile);
		} catch (FileNotFoundException e) {
			// no properties file exists
			int option = JOptionPane
					.showConfirmDialog(
							null,
							"Video lookup service configuration not found. Would you like to configure it now?",
							"VLS Config Missing", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
			switch (option) {
			case JOptionPane.YES_OPTION:
				String upcKey = JOptionPane
						.showInputDialog("Enter your searchupc.com API key");
				// returns null if the user pressed cancel
				if (upcKey == null) {
					JOptionPane
							.showMessageDialog(null,
									"User input cancelled, creating configuration template.");
					createVLSCfgTemplate(vlsPropsFile);
					break;
				}
				// returns null if the user pressed cancel
				String rotTomKey = JOptionPane
						.showInputDialog("Enter your Rotten Tomatoes API key");
				if (rotTomKey == null) {
					JOptionPane
							.showMessageDialog(null,
									"User input cancelled, creating configuration template.");
					createVLSCfgTemplate(vlsPropsFile);
					break;
				}
				// we have both keys, write the properties file
				vlsProps.put(UPC_API_TOKEN, upcKey);
				vlsProps.put(TOMATO_API_TOKEN, rotTomKey);
				FileWriter writer = null;
				try {
					writer = new FileWriter(vlsPropsFile);
					vlsProps.store(writer, "Video Lookup Service Configuration");
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(
							null,
							"Unable to write VLS configuration. "
									+ ioe.getMessage(),
							"Error Saving Configuration",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e1) {
							// closing
						}
					}
				}
				break;
			case JOptionPane.NO_OPTION:
				// create the file for later configuration
				createVLSCfgTemplate(vlsPropsFile);
			}
		}
		try {
			if (reader == null) {
				// the file should exist at this point since we created it above
				JOptionPane
				.showMessageDialog(null,
						"Configuration file created at: " + vlsPropsFile.getAbsolutePath());
				reader = new FileReader(vlsPropsFile);
			}
			vlsProps.load(reader);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null,
					"Unable to read VLS configuration. " + ioe.getMessage(),
					"Error Reading Configuration", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// closing
				}
			}
		}
		return vlsProps;
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
		MobileServicesImpl serviceImpl = new MobileServicesImpl(getModel());
		serviceImpl.setVideoLookupService(videoLookup);
		wsEndpoint = Endpoint.create(serviceImpl);
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

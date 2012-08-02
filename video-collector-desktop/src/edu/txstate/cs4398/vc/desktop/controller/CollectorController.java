package edu.txstate.cs4398.vc.desktop.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
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

	/**
	 * Creates a new application controller.
	 */
	public CollectorController() {
		// set the application model
		setModel(new CollectorModel());

		// create the view and make it visible
		setView(new CollectorView(getModel(), this));
		getView().setVisible(true);

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
	}

	public String getIPAddress() {
		return NetworkUtils.getIPAddress();
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
	 * Saves the collection. If no file is selected, the user is presented a
	 * file selection dialog to pick the save file.
	 */
	public void save() {

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

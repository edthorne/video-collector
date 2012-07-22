/**
 * 
 */
package edu.txstate.cs4398.vc.desktop;

import java.io.IOException;

import javax.xml.ws.Endpoint;

import edu.txstate.cs4398.vc.desktop.services.MobileSerivcesImpl;
import edu.txstate.cs4398.vc.desktop.utils.DiscoveryListener;

/**
 * @author Ed
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// start webservice
		Endpoint.publish("http://localhost:8796/MobileServices", new MobileSerivcesImpl());
		// start discovery listener
		try {
			DiscoveryListener listener = new DiscoveryListener();
			listener.start();
		} catch (IOException e) {
			System.err.println("There was a problem starting the DiscoveryListener");
			e.printStackTrace();
		}
	}

}

/**
 * 
 */
package edu.txstate.cs4398.vc.desktop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.ws.Endpoint;

import edu.txstate.cs4398.vc.desktop.services.MobileServicesImpl;
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
		String ipAddr = null;
		try {
		    InetAddress addr = InetAddress.getLocalHost();

		    // Get IP Address
		    ipAddr = addr.getHostAddress();
		} catch (UnknownHostException e) {
			System.err.println("There was a problem getting localhost ip address");
		}

		Endpoint.publish("http://"+ipAddr+":8796/MobileServices", new MobileServicesImpl());
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

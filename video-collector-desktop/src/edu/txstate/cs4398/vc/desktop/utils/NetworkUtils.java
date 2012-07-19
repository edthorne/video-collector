/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Ed
 * 
 */
public class NetworkUtils {
	/**
	 * Gets a list of network interfaces appropriate to search.
	 * 
	 * @return a list of network interfaces to search or an empty list if no
	 *         networks are available to search
	 */
	public static List<NetworkInterface> getInterfaces() {
		List<NetworkInterface> results = new ArrayList<NetworkInterface>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface netInf = interfaces.nextElement();
				if (!netInf.isUp() || netInf.isLoopback()
						|| !netInf.supportsMulticast())
					continue;
				results.add(netInf);
			}
		} catch (SocketException e) {
			// if for some reason we can't get the interfaces, return an empty
			// list
			e.printStackTrace();
		}
		return results;
	}
}

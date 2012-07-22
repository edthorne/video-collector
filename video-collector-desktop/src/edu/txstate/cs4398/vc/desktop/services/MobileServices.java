package edu.txstate.cs4398.vc.desktop.services;

import javax.jws.WebService;

@WebService
public interface MobileServices {
	/**
	 * Returns the data back to the client.
	 * 
	 * @param data from the client
	 * @return the data from the client
	 */
	String echo(String data);
}

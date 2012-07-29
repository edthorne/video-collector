package edu.txstate.cs4398.vc.desktop.services;

import javax.jws.WebService;

import edu.txstate.cs4398.vc.model.Video;

@WebService
public interface MobileServices {
	/**
	 * Returns the data back to the client.
	 * 
	 * @param data from the client
	 * @return the data from the client
	 */
	public String echo(String data);
	
	public Video getVideoByUPC(String upc);
	
	public String getProductName(String upc);
	
	public Video getVideoByName(String name);
}

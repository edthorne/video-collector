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
	
	/**
	 * Returns a Video to the client
	 * @param UPC code as String
	 * @return video with as much info as available
	 */
	public Video getVideoByUPC(String upc);
	
	/**
	 * Returns a produt name given a UPC
	 * @param upc
	 * @return
	 */
	public String getProductName(String upc);
	
	/**
	 * Returns a Video given a video title
	 * @param name
	 * @return
	 */
	public Video getVideoByName(String name);
	
	/**
	 * @param upc 
	 * @param title
	 * @return true if added to the collection
	 */
	public boolean addVideo(String upc, String title);
}

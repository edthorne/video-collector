package edu.txstate.cs4398.vc.desktop.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import edu.txstate.cs4398.vc.model.Rating;
import edu.txstate.cs4398.vc.model.Video;

@WebService (serviceName = "MobileServices")
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
	 * Returns a product name given a UPC
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
	 * adds a video to the desktop collection
	 * @param upc
	 * @param title
	 * @param Director
	 * @param rated
	 * @param runtime
	 * @param year
	 * @return
	 */
	@WebMethod (operationName="addVideo")
	public String addVideo(@WebParam(name="upc") String upc, @WebParam(name="title")String title, 
							@WebParam(name="director")String director,@WebParam(name="rated") Rating rated, 
							@WebParam(name="runtime")int runtime,@WebParam(name="year") int year);
}

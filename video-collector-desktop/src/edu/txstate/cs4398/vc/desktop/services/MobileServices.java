package edu.txstate.cs4398.vc.desktop.services;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import edu.txstate.cs4398.vc.model.Collection;
import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Rating;
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
	public Video lookupVideoByUPC(String upc);
	
	
	/**
	 * Returns a Video given a video title
	 * @param name
	 * @return
	 */
	public Video lookupVideoByTitle(String title);
	
	/**
	 * adds a video to the desktop collection
	 * @param upc
	 * @param title
	 * @param Director
	 * @param rated
	 * @param runtime
	 * @param year
	 * @return "success" if there are no errors
	 */
	@WebMethod (operationName="addVideo")
	public String addVideo(@WebParam(name="upc") String upc, @WebParam(name="title")String title, 
							@WebParam(name="director")String director,@WebParam(name="rated") Rating rated, 
							@WebParam(name="runtime")int runtime,@WebParam(name="year") int year, 
							@WebParam(name="imgUrl") String imgUrl, @WebParam(name="image") String image);
	
	/**
	 * returns the collection from the desktop application
	 * @return
	 */
	@WebMethod (operationName="getCollection")
	public Collection getCollection();
	
	/**
	 * returns the categories from the desktop application
	 * @return
	 */
	@WebMethod (operationName="getCategories")
	public Set<String> getCategories();

	/**
	 * returns the "people" (directors) from the desktop application
	 * @return
	 */
	@WebMethod (operationName="getPeople")
	public Set<Person> getPeople();

}

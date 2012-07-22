package edu.txstate.cs4398.vc.desktop.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import edu.txstate.cs4398.vc.model.Category;
import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Rating;
import edu.txstate.cs4398.vc.model.Video;

public class VideoLookupService {
	
	private Properties prop = new Properties();
	private String access_token;
	private String tomato_token;
	
	public VideoLookupService() throws FileNotFoundException, IOException
	{
		prop.load(new FileInputStream("config/token.properties"));
		tomato_token = prop.getProperty("tomato_api_token");
		access_token = prop.getProperty("upc_api_token");
	}

	/**
	 * Gets the product name associated with a upc code from the searchupc.com web service
	 * @param upc	- The upc of the product
	 * @param access_token
	 * @return The full product name as a string
	 * @throws IOException if there is a problem establishing connection with the service
	 * @throws JSONException if nothing is found
	 */
	public String getProductName(String upc) throws IOException, JSONException
	{
		URL upcURL = null;
		try {
			upcURL = new URL("http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token="+ access_token + "&upc=" + upc);
		} catch (MalformedURLException e1) {
			throw new IOException();
		}
		
		URLConnection upcCon = upcURL.openConnection();

		//System.out.println(upcURL.toString());
	    BufferedReader in = new BufferedReader(new InputStreamReader(upcCon.getInputStream()));

	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject upcResponse = new JSONObject(builder.toString());
	    //System.out.println(upcResponse);
	   // System.out.println(upcResponse.getJSONObject("0").getString("productname"));
	    String videoName = upcResponse.getJSONObject("0").getString("productname");
	    
	    return videoName;
	}
	
	/**
	 * Returns a video after searching web services for information
	 * Uses rotten tomatoes API first, followed by imdb if nothing found
	 * @param videoName
	 * @return a video object - will be empty title if no result found.
	 * @throws IOException if there is a problem establishing the connection with the web service
	 * @throws JSONException
	 * @throws Exception
	 */
	public Video getVideoByName(String videoName) throws IOException, JSONException
	{
		videoName = videoName.toLowerCase();
		//System.out.println(videoName);
		if(videoName.contains("("))
			videoName = videoName.substring(0, videoName.indexOf("("));
		//System.out.println(videoName);
		if(videoName.contains("collector"))
			videoName = videoName.substring(0, videoName.indexOf("collector"));
			
		videoName = URLEncoder.encode(videoName, "UTF-8");
		URL tomatoURL;
		try {
			tomatoURL = new URL("http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" +
								tomato_token +"&q="+videoName+"&page_limit=1");
		} catch (MalformedURLException e) {
			throw new IOException();
		}
		
		//System.out.println(dvdURL.toString());
		URLConnection upcCon = tomatoURL.openConnection();
		
	    BufferedReader in = new BufferedReader(new InputStreamReader(upcCon.getInputStream()));

	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject tomatoResponse;
	    Video videoObject = new Video(0,"");

	    try {
			tomatoResponse = new JSONObject(builder.toString());
		} catch (JSONException e) {
			try {
				System.out.println(videoName);
				return imdbLookupService(videoName, videoObject);
			} catch (Exception e1) {
				return videoObject;
			}
		}
	    try {
	    	return transformTomatoResponse(tomatoResponse, videoObject);
	    }catch (JSONException e) {
			try {
				return imdbLookupService(videoName, videoObject);
			} catch (Exception e1) {
				return videoObject;
			}
		}
	}
	
	private Video transformTomatoResponse(JSONObject tomatoResponse, Video videoObject) throws JSONException
	{
		tomatoResponse = tomatoResponse.getJSONArray("movies").getJSONObject(0);
	    videoObject.setTitle(tomatoResponse.getString("title"));
	    videoObject.setRuntime(Integer.parseInt(tomatoResponse.getString("runtime")));
	    
	    System.out.println("Rotten Tomatoes Response:");
	    System.out.println("Title: " + tomatoResponse.getString("title"));
	    System.out.println("MPAA Rating: " + tomatoResponse.getString("mpaa_rating"));
	    System.out.println("Year: " + tomatoResponse.getString("year"));
	    try {
			System.out.println("Director: " + getDirector(tomatoResponse.getLong("id")));
		} catch (Exception e) {
			System.out.println("Director: Not Found");
		}
	    System.out.println();
	    
	    return videoObject;	    
	}
	
	private Video imdbLookupService(String videoName, Video videoObject) throws Exception
	{
		URL imdbURL = new URL("http://www.imdbapi.com/?i=&t=" + videoName);
		URLConnection imdbCon = imdbURL.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(imdbCon.getInputStream()));

	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject imdbResponse = new JSONObject(builder.toString());
	    
	    videoObject.setTitle(imdbResponse.getString("Title"));

	    System.out.println("IMDB Lookup Response:");
	    System.out.println("Title: " + imdbResponse.getString("Title"));
	    System.out.println("MPAA Rating: " + imdbResponse.getString("Rated"));
	    System.out.println("Year: " + imdbResponse.getString("Year"));
	    System.out.println("Director: " + imdbResponse.getString("Director"));
	    System.out.println();

		return videoObject;
	}
	
	private String getDirector(long movieId) throws Exception
	{
		URL serviceURL = new URL("http://api.rottentomatoes.com/api/public/v1.0/movies/" + movieId + ".json?apikey=" + tomato_token);
		//System.out.println(imdbURL.toString());
		URLConnection serviceCon = serviceURL.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(serviceCon.getInputStream()));
	    
	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject tomatoResponse = new JSONObject(builder.toString());
	    
	    tomatoResponse = tomatoResponse.getJSONArray("abridged_directors").getJSONObject(0);
	    return tomatoResponse.getString("name");
	    
	}


}

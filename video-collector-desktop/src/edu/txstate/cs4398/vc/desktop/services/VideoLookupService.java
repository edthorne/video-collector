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

import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Rating;
import edu.txstate.cs4398.vc.model.Video;

/**
 * 
 * @author mnosler
 *
 */
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

	    BufferedReader in = new BufferedReader(new InputStreamReader(upcCon.getInputStream()));

	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject upcResponse = new JSONObject(builder.toString());
	    String productName = upcResponse.getJSONObject("0").getString("productname");
	    
	    return productName;
	}
	
	/**
	 * Returns a video after searching web services for information
	 * Uses rotten tomatoes API first, followed by imdb if nothing found
	 * If nothing is found on either service, the existing video will be returned
	 * @param videoName
	 * @param videoObject - a video object that already has its unique ID assigned
	 * @return a video object - will be empty title if no result found.
	 * @throws IOException if there is a problem establishing the connection with the web service
	 * @throws Exception
	 */
	public Video getVideoByName(String videoName, Video videoObject) throws IOException
	{
		videoName = videoName.toLowerCase();
		if(videoName.contains("("))
			videoName = videoName.substring(0, videoName.indexOf("("));
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
		
		URLConnection upcCon = tomatoURL.openConnection();
		
	    BufferedReader in = new BufferedReader(new InputStreamReader(upcCon.getInputStream()));

	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject tomatoResponse;

	    try {
			tomatoResponse = new JSONObject(builder.toString());
		} catch (JSONException e) {
			try {
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
	    videoObject.setRated(getRating(tomatoResponse.getString("mpaa_rating")));
	    videoObject.setYear(Integer.parseInt(tomatoResponse.getString("year")));
	    try {
	    	videoObject.setDirector(getDirectorTomatoes(tomatoResponse.getLong("id")));
		} catch (Exception e) {
			videoObject.setDirector(null);
		}
	    
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
	    videoObject.setRated(getRating(imdbResponse.getString("Rated")));
	    videoObject.setYear(Integer.parseInt(imdbResponse.getString("Year")));
	    String directorName = imdbResponse.getString("Director");
	    
	    String[] nameSplit = directorName.split(" ");
	    if(nameSplit.length > 2)
	    {
	    	String firstName = nameSplit[0];
	    	String lastName = nameSplit[1];
	    	videoObject.setDirector(new Person(lastName, firstName));
	    }
	    else
	    	videoObject.setDirector(null);
	    
	    videoObject.setRuntime(getImdbRuntime(imdbResponse.getString("Runtime")));


		return videoObject;
	}
	

	private Person getDirectorTomatoes(long movieId) throws Exception
	{
		URL serviceURL = new URL("http://api.rottentomatoes.com/api/public/v1.0/movies/" + movieId + ".json?apikey=" + tomato_token);
		URLConnection serviceCon = serviceURL.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(serviceCon.getInputStream()));
	    
	    String inputLine;
	    StringBuilder builder = new StringBuilder();

	    while ((inputLine = in.readLine()) != null) 
	        builder.append(inputLine);
	    in.close();

	    JSONObject tomatoResponse = new JSONObject(builder.toString());
	    
	    tomatoResponse = tomatoResponse.getJSONArray("abridged_directors").getJSONObject(0);
	    String directorName = tomatoResponse.getString("name");
	    
	    String[] nameSplit = directorName.split(" ");
	    String firstName = nameSplit[0];
	    String lastName = nameSplit[1];
	    return new Person(lastName,firstName);
	}
	
	private Rating getRating(String rating)
	{
		if(rating.equals("G"))
			return Rating.G;
		else if(rating.equals("PG"))
			return Rating.PG;
		else if(rating.equals("PG-13"))
			return Rating.PG13;
		else if(rating.equals("R"))
			return Rating.R;
		else if(rating.equals("NC-17"))
			return Rating.NC17;
		else 
			return Rating.NR;
		
	}
	
	public int getImdbRuntime(String string) {
		string = string.replace(" ", "");
		string = string.replaceAll("h|min", " ");
		string = string.trim();

		String[] divided = string.split(" ");
		
		int runtime = 0;
		
		if(divided.length>1)
		{
			runtime += 60*Integer.parseInt(divided[0]);
			runtime += Integer.parseInt(divided[1]);
		}
		else
			runtime = Integer.parseInt(string);
		return runtime;
	}


}

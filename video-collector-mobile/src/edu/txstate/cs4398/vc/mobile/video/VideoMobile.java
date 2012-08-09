package edu.txstate.cs4398.vc.mobile.video;


import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import edu.txstate.cs4398.vc.model.AbstractModel;
import edu.txstate.cs4398.vc.model.ModelEvent;
import edu.txstate.cs4398.vc.model.Video;

/**
 * Represents a video without JAXB annotation
 * @author mnosler
 */
@Root
public class VideoMobile extends AbstractModel {
	/**
	 * Event identifier for property changes.
	 */
	public static final int PROPERTY_CHANGED = 1;

	@Element(required = false)
	private String upc;
	@Element(required = true)
	private String title;
	@Element(required = false)
	private String director;
	@Element
	private int year;
	@Element(required = false)
	private String rated;
	@Element
	private int runtime;
	@Element
	private byte myRating;
	@Element(required = false)
	private String notes;
	@Element(required = false)
	private String category;
	@Element(required = false)
	private String imageURL;

	@Element(required = false)
	private byte[] imageBytes;


	/**
	 * Creates a new video. You must set a title before marshaling the video.
	 */
	public VideoMobile() {
		// default constructor
	}
	
	/**
	 * Creates a new video. You must set a title before marshaling the video.
	 */
	public VideoMobile(Video video) {
		// default constructor
		upc = video.getUpc();
		director = video.getDirector().getFirstName() + " " + video.getDirector().getLastName();
		title = video.getTitle();
		year = video.getYear();
		rated = video.getRated().toString();
		runtime = video.getRuntime();
		myRating = video.getMyRating();
		notes = video.getNotes();
		category = video.getCategory();
		imageURL = video.getImageURL();
		imageBytes = video.getImage();
	}
	
	/**
	 * returns a video given a soap object containing all values from web service response
	 * @param response
	 * @return
	 */
	public static VideoMobile getVideoFromSoap(SoapObject response) {
		VideoMobile video = new VideoMobile();
		String title = response.getPropertySafelyAsString("title");
		video.setTitle(title);

		SoapObject director = (SoapObject) response.getPropertySafely(
				"director", new SoapObject("", ""));
		if (!director.toString().isEmpty()) {
			String first = director.getPropertySafelyAsString("firstName", "");
			String last = director.getPropertySafelyAsString("lastName", "");
			if (!(last.isEmpty() && first.isEmpty()) && !"N/A".equals(last)) {
				video.setDirector(first + " " + last);
			}

		}

		String rated = response.getPropertySafelyAsString("rated", "UNRATED");
		video.setRated(rated);
		int year = Integer.parseInt(response.getPropertySafelyAsString("year"));
		video.setYear(year);
		int runtime = Integer.parseInt(response
				.getPropertySafelyAsString("runtime"));
		video.setRuntime(runtime);
		Log.d("adding video", video.getTitle());
		video.setImageURL(response.getPropertySafelyAsString("imageURL"));

		String notes = response.getPropertySafelyAsString("notes");
		video.setNotes(!notes.contains("anyType{}") ? notes : "");
		
		video.setCategory(response.getPropertySafelyAsString("category"));
		video.setMyRating(Byte.parseByte(response.getPropertySafelyAsString("myRating","0")));
		video.setImageBytes(Base64.decode(response.getPropertySafelyAsString("image", "")));

		return video;

	}

	/**
	 * Creates a new video with the given title.
	 * 
	 * @param title
	 *            the title of the new video
	 */
	public VideoMobile(String title) {
		this.title = title;
	}

	/**
	 * @return the video title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the video title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "title"));
	}

	/**
	 * @return the year the video was released
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year the video was released.
	 * 
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "year"));
	}

	/**
	 * @return the runtime of the video in minutes
	 */
	public int getRuntime() {
		return runtime;
	}

	/**
	 * Sets the runtime of the video in minutes.
	 * 
	 * @param runtime
	 *            the runtime to set in minutes
	 */
	public void setRuntime(int runtime) {
		this.runtime = runtime;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "runtime"));
	}

	/**
	 * @return the UPC code of the video
	 */
	public String getUpc() {
		return upc;
	}

	/**
	 * Sets the UPC code of the video.
	 * 
	 * @param upc
	 *            the UPC code to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "upc"));
	}

	/**
	 * @return the personal rating of the video
	 */
	public byte getMyRating() {
		return myRating;
	}

	/**
	 * Sets the personal rating of the video.
	 * 
	 * @param myRating
	 *            the rating to set
	 */
	public void setMyRating(byte myRating) {
		if (myRating < 0 || myRating > 5) {
			throw new IllegalArgumentException();
		}
		this.myRating = myRating;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "myRating"));
	}

	/**
	 * @return the notes for the video
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Sets the notes for the video.
	 * 
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "notes"));
	}

	/**
	 * @return the director of the video
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * Sets the director of the video.
	 * 
	 * @param director
	 *            the director to set
	 */
	public void setDirector(String director) {
		this.director = director;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "director"));
	}

	/**
	 * @return the movie rating
	 */
	public String getRated() {
		return rated;
	}

	/**
	 * Sets the movie rating.
	 * 
	 * @param rated
	 *            the rating to set
	 */
	public void setRated(String rated) {
		this.rated = rated;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "rated"));
	}

	/**
	 * @return the movie category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the movie category.
	 * 
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "category"));
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}


	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
}

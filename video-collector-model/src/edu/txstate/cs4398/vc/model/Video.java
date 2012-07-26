package edu.txstate.cs4398.vc.model;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "video")
@XmlType(propOrder = { "upc", "title", "director", "year", "rated",
		"runtime", "category", "myRating", "notes" })
public class Video extends AbstractModel {
	private UUID videoId;
	private String title;
	private int year;
	private int runtime;
	private String upc;
	private byte myRating;
	private String notes;
	private Person director;
	private Rating rated;
	private Category category;

	public Video() {
		// default constructor
	}
	
	public Video(String title) {
		this.title = title;
	}

	@XmlID
	@XmlAttribute
	public String getVideoId() {
		if (videoId == null) {
			return null;
		}
		return videoId.toString();
	}

	public void setVideoId(String videoId) {
		this.videoId = UUID.fromString(videoId);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public byte getMyRating() {
		return myRating;
	}

	public void setMyRating(byte myRating) {
		if (myRating < 0 || myRating > 5) {
			throw new IllegalArgumentException();
		}
		this.myRating = myRating;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Person getDirector() {
		return director;
	}

	public void setDirector(Person director) {
		// remove prior director if set
		if (this.director != null) {
			this.director.removeVideo(this);
		}
		this.director = director;
		// add this video to the director
		if (this.director != null) {
			this.director.addVideo(this);
		}
	}

	public Rating getRated() {
		return rated;
	}

	public void setRated(Rating rated) {
		this.rated = rated;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
package edu.txstate.cs4398.vc.model;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "video")
@XmlType(propOrder = { "upc", "title", "director", "year", "rated", "runtime",
		"category", "myRating", "notes" })
public class Video extends AbstractModel {
	public static final int PROPERTY_CHANGED = 1;
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
			return "";
		}
		return videoId.toString();
	}

	void setVideoId(String videoId) {
		this.videoId = UUID.fromString(videoId);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "title"));
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "year"));
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "runtime"));
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "upc"));
	}

	public byte getMyRating() {
		return myRating;
	}

	public void setMyRating(byte myRating) {
		if (myRating < 0 || myRating > 5) {
			throw new IllegalArgumentException();
		}
		this.myRating = myRating;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "myRating"));
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "notes"));
	}

	@XmlIDREF
	public Person getDirector() {
		return director;
	}

	public void setDirector(Person director) {
		// remove prior director if set
		if (this.director != null) {
			this.director.removeVideoFromDirector(this);
		}
		this.director = director;
		// add this video to the director
		if (this.director != null) {
			this.director.addVideoToDirector(this);
		}
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "director"));
	}

	public Rating getRated() {
		return rated;
	}

	public void setRated(Rating rated) {
		this.rated = rated;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "rated"));
	}

	@XmlIDREF
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		// remove prior category if set
		if (this.category != null) {
			this.category.removeVideoFromCategory(this);
		}
		this.category = category;
		// add this video to the category
		if (this.category != null) {
			this.category.addVideoToCategory(this);
		}
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "category"));
	}
}
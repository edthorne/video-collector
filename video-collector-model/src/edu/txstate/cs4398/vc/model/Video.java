package edu.txstate.cs4398.vc.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "video")

@XmlType(propOrder = {"videoId", "upc", "title", "director",  "year", "rated", "runtime", "category", "myRating", "notes" })
public class Video extends AbstractModel {
	private int videoId;
	private String title;
	private int year;
	private int runtime;
	private long upc;
	private byte myRating;
	private String notes;
	private Person director;
	private Rating rated;
	private Category category;

	public Video()
	{
	}
	
	public Video(int videoId, String title) {
		this.videoId = videoId;
		this.title = title;
	}
	
	public Video(int videoId, String title, int year, int runtime, long upc, Person director, Rating rated, Category category)
	{
		this.myRating = 0;
		this.notes = "";
		this.videoId = videoId;
		this.title = title;
		this.year = year;
		this.runtime = runtime;
		this.upc = upc;
		this.director = director;
		this.rated = rated;
		this.category = category;	
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
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

	public long getUpc() {
		return upc;
	}

	public void setUpc(long upc) {
		this.upc = upc;
	}

	public byte getMyRating() {
		return myRating;
	}

	public void setMyRating(byte myRating) {
		this.myRating = myRating;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Person getDirector()
	{
		return director;
	}
	
	public void setDirector(Person director)
	{
		this.director = director;
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
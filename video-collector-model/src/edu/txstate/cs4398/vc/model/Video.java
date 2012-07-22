package edu.txstate.cs4398.vc.model;

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

	public Video(int videoId, String title) {
		this.videoId = videoId;
		this.title = title;
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
		throw new UnsupportedOperationException();
	}

	public void setMyRating(byte myRating) {
		throw new UnsupportedOperationException();
	}

	public String getNotes() {
		throw new UnsupportedOperationException();
	}

	public void setNotes(String notes) {
		throw new UnsupportedOperationException();
	}
}
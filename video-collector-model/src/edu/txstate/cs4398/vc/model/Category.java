package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "category")
public class Category extends AbstractModel {
	public static final int PROPERTY_CHANGED = 1;
	public static final int VIDEO_ADDED = 2;
	public static final int VIDEO_REMOVED = 3;

	private UUID categoryId;
	private String name;
	@XmlIDREF
	@XmlElementWrapper
	@XmlElement(name = "video")
	private List<Video> videos = new ArrayList<Video>();

	public Category() {
		// default constructor
	}

	public Category(String name) {
		this.name = name;
	}

	@XmlID
	@XmlAttribute
	public String getCategoryId() {
		if (categoryId == null) {
			return "";
		}
		return categoryId.toString();
	}

	void setCategoryId(String categoryId) {
		this.categoryId = UUID.fromString(categoryId);
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "name"));
	}

	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	public void addVideo(Video video) {
		// delegate to video to set the category
		video.setCategory(this);
	}

	public void removeVideo(Video video) {
		// delegate to video to remove the category
		video.setCategory(null);
	}

	void addVideoToCategory(Video video) {
		// called by video.setDirector
		videos.add(video);
		notifyChanged(new ModelEvent(this, VIDEO_ADDED, video.getTitle()));
	}

	void removeVideoFromCategory(Video video) {
		// called by video.setDirector
		videos.remove(video);
		notifyChanged(new ModelEvent(this, VIDEO_REMOVED, video.getTitle()));
	}
}
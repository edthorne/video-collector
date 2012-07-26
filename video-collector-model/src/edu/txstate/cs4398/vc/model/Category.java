package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "category")
public class Category extends AbstractModel {
	private UUID categoryId;
	private String name;
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
			return null;
		}
		return categoryId.toString();
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = UUID.fromString(categoryId);
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	}

	void removeVideoFromCategory(Video video) {
		// called by video.setDirector
		videos.remove(video);
	}
}
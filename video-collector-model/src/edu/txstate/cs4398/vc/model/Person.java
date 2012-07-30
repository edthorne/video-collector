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
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "person")
@XmlType(propOrder = { "lastName", "firstName", "directedVideos" })
public class Person extends AbstractModel {
	public static final int PROPERTY_CHANGED = 1;
	public static final int VIDEO_ADDED = 2;
	public static final int VIDEO_REMOVED = 3;

	private UUID personId;
	private String lastName;
	private String firstName;
	@XmlIDREF
	@XmlElementWrapper
	@XmlElement(name = "video")
	private List<Video> directedVideos = new ArrayList<Video>();

	public Person() {
		// default constructor
	}

	public Person(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}

	@XmlID
	@XmlAttribute
	public String getPersonId() {
		if (personId == null) {
			return "";
		}
		return personId.toString();
	}

	void setPersonId(String personId) {
		this.personId = UUID.fromString(personId);
	}

	@XmlElement
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "lastName"));
	}

	@XmlElement
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "firstName"));
	}

	public List<Video> getDirectedVideos() {
		return Collections.unmodifiableList(directedVideos);
	}

	public void addDirectedVideo(Video video) {
		// delegate to video to set the director
		video.setDirector(this);
	}

	public void removeDirectedVideo(Video video) {
		// delegate to video to remove the director
		video.setDirector(null);
	}

	void addVideoToDirector(Video video) {
		// called by video.setDirector
		directedVideos.add(video);
		notifyChanged(new ModelEvent(this, VIDEO_ADDED, video.getTitle()));
	}

	void removeVideoFromDirector(Video video) {
		// called by video.setDirector
		directedVideos.remove(video);
		notifyChanged(new ModelEvent(this, VIDEO_REMOVED, video.getTitle()));
	}
}
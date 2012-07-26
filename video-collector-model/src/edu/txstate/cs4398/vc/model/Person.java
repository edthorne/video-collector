package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "person")
@XmlType(propOrder = { "lastName", "firstName", "directedVideos" })
public class Person extends AbstractModel {
	private UUID personId;
	private String lastName;
	private String firstName;
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
			return null;
		}
		return personId.toString();
	}

	public void setPersonId(String personId) {
		this.personId = UUID.fromString(personId);
	}

	@XmlElement
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlIDREF
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
	}

	void removeVideoFromDirector(Video video) {
		// called by video.setDirector
		directedVideos.remove(video);
	}
}
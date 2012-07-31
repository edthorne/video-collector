package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.junit.experimental.categories.Category;

@XmlRootElement(name = "Video_Collection")
public class Collection extends AbstractModel implements ModelListener {
	public static final int PROPERTY_CHANGED = 1;
	public static final int CATEGORY_ADDED = 2;
	public static final int CATEGORY_REMOVED = 3;
	public static final int PERSON_ADDED = 4;
	public static final int PERSON_REMOVED = 5;
	public static final int VIDEO_ADDED = 6;
	public static final int VIDEO_REMOVED = 7;
	private String name;
	/**
	 * Useful for searching videos by rating.
	 */
	private Map<Rating, List<? extends Video>> ratedVideos;
	/**
	 * Useful for searching videos by category.
	 */
	private Map<String, List<? extends Video>> categorizedVideos;

	@XmlElementWrapper(name = "videos")
	@XmlElement(name = "video")
	private List<CollectionVideo> videos = new ArrayList<CollectionVideo>();

	@XmlElementWrapper(name = "people")
	@XmlElement(name = "person")
	private List<CollectionPerson> people = new ArrayList<CollectionPerson>();

	public Collection() {
		// default constructor
		categorizedVideos = new HashMap<String, List<? extends Video>>();
	}

	public Collection(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "name"));
	}

	public Person addPerson(Person person) {
		// if we have a person with matching names, return that one
		CollectionPerson searchResult = findPerson(person);
		if (searchResult != null) {
			return searchResult;
		}
		// create collection person
		CollectionPerson cp = new CollectionPerson(person);
		// add this person as a new person
		people.add(cp);
		notifyChanged(new ModelEvent(this, PERSON_ADDED, cp.getLastName()
				+ ", " + person.getFirstName()));
		return cp;
	}

	public Video addVideo(Video video) {
		// if we have a video with matching title, return that one
		Video searchResult = findVideo(video);
		if (searchResult != null) {
			return searchResult;
		}
		// create collection video
		CollectionVideo cv = new CollectionVideo(video);
		cv.setDirector(addPerson(video.getDirector()));
		// add this video as a new video
		videos.add(cv);
		cv.addModelListener(this);
		notifyChanged(new ModelEvent(this, VIDEO_ADDED, cv.getTitle()));
		return video;
	}

	public Set<String> getCategories() {
		return Collections.unmodifiableSet(categorizedVideos.keySet());
	}

	public List<? extends Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	public List<? extends Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	public void removePerson(Person person) {
		// if we have a person with matching names, return that one
		CollectionPerson searchResult = findPerson(person);
		if (searchResult == null) {
			// no matching person in collection
			return;
		}
		people.remove(person);
		notifyChanged(new ModelEvent(this, PERSON_REMOVED, person.getLastName()
				+ ", " + person.getFirstName()));
	}

	public void removeVideo(Video video) {
		video.setCategory(null);
		video.setDirector(null);
		removeModelListener(this);
		videos.remove(video);
		notifyChanged(new ModelEvent(this, VIDEO_REMOVED, video.getTitle()));
	}

	public List<Video> searchVideos(SearchCriteria criteria) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void modelChanged(ModelEvent event) {
		// we're listening for changes to video objects
		Object source = event.getSource();
		if (source instanceof CollectionVideo) {
			CollectionVideo video = (CollectionVideo) source;
			switch (event.getID()) {
			case CollectionVideo.PROPERTY_CHANGED:
				// check the action for the property that changed
			}
		}
	}

	private CollectionPerson findPerson(Person person) {
		// if searching for null, return null
		if (person == null) {
			return null;
		}
		// we need one of the two names set
		String pLast = person.getLastName();
		String pFirst = person.getFirstName();
		if (pLast == null && pFirst == null) {
			throw new IllegalArgumentException(
					"One of last name or first name is required");
		}
		// not the fastest implementation but it's a start
		for (CollectionPerson cp : people) {
			String cpLast = cp.getLastName();
			String cpFirst = cp.getFirstName();
			if ((cpLast == null && pLast != null)
					|| (cpLast != null && pLast == null)) {
				// can't be the same since only one of them has a null for a
				// last name
				continue;
			} else if (!(cpLast.equals(pLast))) {
				// last names don't match
				continue;
			} else if ((cpFirst == null && pFirst != null)
					|| (cpFirst != null && pFirst == null)) {
				// can't be the same since only one of them has a null for a
				// first name
				continue;
			} else if (!(cpFirst.equals(pFirst))) {
				// first names don't match
				continue;
			} else {
				// if we got here, first and last match
				return cp;
			}
		}
		// we didn't find a collection person
		return null;
	}

	private Video findVideo(Video video) {
		if (video == null) {
			throw new IllegalArgumentException("Title cannot be null");
		}
		// not the fastest implementation but it's a start
		for (CollectionVideo cv : videos) {
			if (cv.equals(cv.getTitle())) {
				return cv;
			}
		}
		// we didn't find a collection video
		return null;
	}

	@XmlRootElement(name = "cVideo")
	public static class CollectionVideo extends Video {
		private String videoId;
		@XmlIDREF @XmlElement(name="directorId")
		private CollectionPerson director;

		public CollectionVideo() {
			// default constructor
		}

		public CollectionVideo(Video video) {
			// set the id
			videoId = UUID.randomUUID().toString();
			// copy attributes
			this.setCategory(video.getCategory());
			this.setMyRating(video.getMyRating());
			this.setNotes(video.getNotes());
			this.setRated(video.getRated());
			this.setRuntime(video.getRuntime());
			this.setTitle(video.getTitle());
			this.setUpc(video.getUpc());
			this.setYear(video.getYear());
		}

		@XmlID
		public String getVideoId() {
			return videoId;
		}

		public void setVideoId(String videoId) {
			this.videoId = videoId;
		}

		@Override @XmlTransient
		public Person getDirector() {
			return director;
		}

		@Override
		public void setDirector(Person director) {
			this.director = (CollectionPerson) director;
		}
	}

	@XmlRootElement(name = "cPerson")
	public static class CollectionPerson extends Person {
		private String personId;

		public CollectionPerson() {
			// default constructor
		}

		public CollectionPerson(Person person) {
			personId = UUID.randomUUID().toString();
//			this.setFirstName(person.getFirstName());
//			this.setLastName(person.getLastName());
		}

		@XmlID
		public String getPersonId() {
			return this.personId;
		}

		public void setPersonId(String personId) {
			this.personId = personId;
		}
	}
}
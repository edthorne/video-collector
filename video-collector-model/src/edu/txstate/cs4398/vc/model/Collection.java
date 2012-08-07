package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "videoCollection")
@XmlRootElement(name = "videoCollection")
public class Collection extends AbstractModel implements ModelListener {
	// event constants
	public static final int PROPERTY_CHANGED = 1;
	public static final int CATEGORY_ADDED = 2;
	public static final int CATEGORY_REMOVED = 3;
	public static final int PERSON_ADDED = 4;
	public static final int PERSON_REMOVED = 5;
	public static final int VIDEO_ADDED = 6;
	public static final int VIDEO_REMOVED = 7;

	/**
	 * The name for the collection.
	 */
	@Element
	private String name;

	@ElementList(name = "categories", entry = "category")
	@XmlElementWrapper(name = "categories")
	@XmlElement(name = "category")
	private Set<String> categories = new HashSet<String>();

	@ElementList(name = "people", entry = "person")
	@XmlElementWrapper(name = "people")
	@XmlElement(name = "person")
	private Set<Person> people = new HashSet<Person>();

	@ElementList(name = "videos", entry = "video")
	@XmlElementWrapper(name = "videos")
	@XmlElement(name = "video")
	private List<Video> videos = new ArrayList<Video>();

	public Collection() {
		// default constructor
		this(null);
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

	public void addCategory(String category) {
		if (category == null) {
			throw new IllegalArgumentException("Cannot add a null category!");
		}
		// add this category as a new category
		boolean added = categories.add(category);
		if (added) {
			notifyChanged(new ModelEvent(this, CATEGORY_ADDED, category));
		}
	}

	public void addPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Cannot add a null person!");
		}
		// add this person as a new person
		boolean added = people.add(person);
		if (added) {
			notifyChanged(new ModelEvent(this, PERSON_ADDED, person.toString()));
		}
	}

	public void addVideo(Video video) {
		if (video == null) {
			throw new IllegalArgumentException("Cannot add a null video!");
		}
		// add this video as a new video
		videos.add(video);
		// listen for changes
		video.addModelListener(this);
		// notify our listeners of the new video
		notifyChanged(new ModelEvent(this, VIDEO_ADDED, video.getTitle()));
		// if the category or director aren't in our sets, add them
		if (video.getCategory() != null
				&& !categories.contains(video.getCategory())) {
			addCategory(video.getCategory());
		}
		if (video.getDirector() != null
				&& !people.contains(video.getDirector())) {
			addPerson(video.getDirector());
		}
	}

	public Set<String> getCategories() {
		return Collections.unmodifiableSet(categories);
	}

	public Set<Person> getPeople() {
		return Collections.unmodifiableSet(people);
	}

	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	public void removeCategory(String category) {
		boolean removed = categories.remove(category);
		if (removed) {
			notifyChanged(new ModelEvent(this, CATEGORY_REMOVED, category));
		}
	}

	public void removePerson(Person person) {
		boolean removed = people.remove(person);
		if (removed) {
			notifyChanged(new ModelEvent(this, PERSON_REMOVED,
					person.toString()));
		}
	}

	public void removeVideo(Video video) {
		// on the off chance someone hands in a null
		if (video == null) {
			return; // simply exit
		}
		video.removeModelListener(this);
		boolean removed = videos.remove(video);
		if (removed) {
			notifyChanged(new ModelEvent(this, VIDEO_REMOVED, video.getTitle()));
		}
	}

	public List<Video> searchVideos(SearchCriteria criteria) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void modelChanged(ModelEvent event) {
		// we're listening for changes to video objects
		Object source = event.getSource();
		if (source instanceof Video) {
			Video video = (Video) source;
			switch (event.getID()) {
			case Video.PROPERTY_CHANGED:
				// check the action for the property that changed
				if ("category".equals(event.getActionCommand())) {
					// category changed, possible add
					if (video.getCategory() != null
							&& !categories.contains(video.getCategory())) {
						addCategory(video.getCategory());
					}
				} else if ("director".equals(event.getActionCommand())) {
					// director changed, possible add
					if (video.getDirector() != null
							&& !people.contains(video.getDirector())) {
						addPerson(video.getDirector());
					}
				}
			}
		}
	}
	
	public void startAllListeners() {
		for(Video v : videos) {
			v.addModelListener(this);
		}
	}
}
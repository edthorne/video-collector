package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import edu.txstate.cs4398.vc.model.Video;
import edu.txstate.cs4398.vc.model.Category;
import edu.txstate.cs4398.vc.model.Person;

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
	 * Useful for searching videos by rating
	 */
	private HashMap<Rating, List<Video>> ratedVideos;
	
	@XmlElementWrapper(name = "videoList")
	@XmlElement(name = "video")
	private ArrayList<Video> videos = new ArrayList<Video>();
	
	@XmlElementWrapper(name = "categoryList")
	@XmlElement(name = "category")
	private ArrayList<Category> categories = new ArrayList<Category>();
	
	@XmlElementWrapper(name = "peopleList")
	@XmlElement(name = "Person")
	private ArrayList<Person> people = new ArrayList<Person>();

	public Collection() {
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

	public Category addCategory(Category category) {
		// if we have a category with a matching name return that one
		Category searchResult = findCategoryByName(category.getName());
		if (searchResult != null) {
			return searchResult;
		}
		// else set the category id
		category.setCategoryId(UUID.randomUUID().toString());
		// add this category as a new category
		categories.add(category);
		notifyChanged(new ModelEvent(this, CATEGORY_ADDED, category.getName()));
		return category;
	}

	public Person addPerson(Person person) {
		// if we have a person with matching names, return that one
		Person searchResult = findPersonByName(person.getLastName(), person.getFirstName());
		if (searchResult != null) {
			return searchResult;
		}
		// else set the person id
		person.setPersonId(UUID.randomUUID().toString());
		// add this person as a new person
		people.add(person);
		notifyChanged(new ModelEvent(this, PERSON_ADDED, person.getLastName() + ", " + person.getFirstName()));
		return person;
	}

	public Video addVideo(Video video) {
		// if we have a video with matching title, return that one
		Video searchResult = findVideoByTitle(video.getTitle());
		if (searchResult != null) {
			return searchResult;
		}
		// else set the video id
		video.setVideoId(UUID.randomUUID().toString());
		// if present and new, add category and director
		if (video.getCategory() != null && video.getCategory().getCategoryId() == "") {
			addCategory(video.getCategory());
		}
		if (video.getDirector() != null && video.getDirector().getPersonId() == "") {
			addPerson(video.getDirector());
		}
		// add this video as a new video
		videos.add(video);
		video.addModelListener(this);
		notifyChanged(new ModelEvent(this, VIDEO_ADDED, video.getTitle()));
		return video;
	}

	public List<Category> getCategories() {
		return Collections.unmodifiableList(categories);
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	public void removeCategory(Category category) {
		List<Video> videos = new ArrayList<Video>(category.getVideos());
		for (Video video : videos) {
			video.setCategory(null);
		}
		categories.remove(category);
		notifyChanged(new ModelEvent(this, CATEGORY_REMOVED, category.getName()));
	}

	public void removePerson(Person person) {
		List<Video> videos = new ArrayList<Video>(person.getDirectedVideos());
		for (Video video : videos) {
			video.setDirector(null);
		}
		people.remove(person);
		notifyChanged(new ModelEvent(this, PERSON_REMOVED, person.getLastName() + ", " + person.getFirstName()));
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
		if (source instanceof Video) {
			Video video = (Video) source;
			switch (event.getID()) {
			case Video.PROPERTY_CHANGED:
				// check the action for the property that changed
				if ("category".equals(event.getActionCommand())) {
					// category changed
					Category category = video.getCategory();
					// if the category is set and has no categoryId
					if (category != null && category.getCategoryId() == "") {
						// add the category and set the categoryId
						addCategory(category);
					}
				} else if ("director".equals(event.getActionCommand())) {
					// director changed
					Person director = video.getDirector();
					// if the direcor is set and has no personId
					if (director != null && director.getPersonId() == "") {
						// add the person and set the personId
						addPerson(director);
					}
				}
			}
		}
		
	}
	
	private Category findCategoryByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		// not the fastest implementation but it's a start
		for (Category category : categories) {
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}
	
	private Person findPersonByName(String lastName, String firstName) {
		if (lastName == null || firstName == null) {
			throw new IllegalArgumentException("Last name and first name cannot be null");
		}
		// not the fastest implementation but it's a start
		for (Person person : people) {
			if (lastName.equals(person.getLastName()) && firstName.equals(person.getFirstName())) {
				return person;
			}
		}
		return null;
	}
	
	private Video findVideoByTitle(String title) {
		if (title == null) {
			throw new IllegalArgumentException("Title cannot be null");
		}
		// not the fastest implementation but it's a start
		for (Video video : videos) {
			if (title.equals(video.getTitle())) {
				return video;
			}
		}
		return null;
	}
}
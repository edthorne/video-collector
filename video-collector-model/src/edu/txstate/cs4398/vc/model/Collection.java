package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import edu.txstate.cs4398.vc.model.Video;
import edu.txstate.cs4398.vc.model.Category;
import edu.txstate.cs4398.vc.model.Person;

@XmlRootElement(name = "Video_Collection")

public class Collection extends AbstractModel {
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

	public Collection(String name) {
		this.name = name;
	}

	public Collection() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addCategory(Category category) {
		for(Category cat : categories)
			if(cat.getCategoryId() == category.getCategoryId())
				return;
		categories.add(category);
	}

	public void addPerson(Person person) {
		for(Person per : people)
			if(per.getPersonId() == person.getPersonId())
				return;
		people.add(person);
	}

	public void addVideo(Video video) {
		for(Video vid : videos)
			if(vid.getVideoId() == video.getVideoId())
				return;
		videos.add(video);
	}

	public List<Category> getCategories() {
		return categories;
	}

	public List<Person> getPeople() {
		return people;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void removeCategory(Category category) {
		categories.remove(category);
	}

	public void removePerson(Person person) {
		categories.remove(person);
	}

	public void removeVideo(Video video) {
		videos.remove(video);
	}

	public List<Video> searchVideos(SearchCriteria criteria) {
		throw new UnsupportedOperationException();
	}
}
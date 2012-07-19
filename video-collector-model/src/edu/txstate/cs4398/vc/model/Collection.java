package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.txstate.cs4398.vc.model.Video;
import edu.txstate.cs4398.vc.model.Category;
import edu.txstate.cs4398.vc.model.Person;

public class Collection extends AbstractModel {
	private String name;
	/**
	 * Useful for searching videos by rating
	 */
	private HashMap<Rating, List<Video>> ratedVideos;
	private ArrayList<Video> videos = new ArrayList<Video>();
	private ArrayList<Category> categories = new ArrayList<Category>();
	private ArrayList<Person> people = new ArrayList<Person>();

	public Collection(String name) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public void addCategory(Category category) {
		throw new UnsupportedOperationException();
	}

	public void addPerson(Person person) {
		throw new UnsupportedOperationException();
	}

	public void addVideo(Video video) {
		throw new UnsupportedOperationException();
	}

	public List<Category> getCategories() {
		throw new UnsupportedOperationException();
	}

	public List<Person> getPeople() {
		throw new UnsupportedOperationException();
	}

	public List<Video> getVideos() {
		throw new UnsupportedOperationException();
	}

	public void removeCategory(Category category) {
		throw new UnsupportedOperationException();
	}

	public void removePerson(Person person) {
		throw new UnsupportedOperationException();
	}

	public void removeVideo(Video video) {
		throw new UnsupportedOperationException();
	}

	public List<Video> searchVideos(SearchCriteria criteria) {
		throw new UnsupportedOperationException();
	}
}
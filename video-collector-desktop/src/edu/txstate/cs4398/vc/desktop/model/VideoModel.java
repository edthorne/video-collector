/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.model;

import java.util.Set;

import edu.txstate.cs4398.vc.model.AbstractModel;
import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Video;

/**
 * A model class for video creation and editing.
 * 
 * @author Ed
 */
public class VideoModel extends AbstractModel {
	/**
	 * The set of categories for the editor.
	 */
	private Set<String> categories;

	/**
	 * A flag to indicate if the video being edited is new.
	 */
	private boolean isNew;

	/**
	 * The set of people for the editor.
	 */
	private Set<Person> people;

	/**
	 * The video being edited or null for a new video.
	 */
	private Video video;

	/**
	 * Creates a new empty video model.
	 */
	public VideoModel(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the categories
	 */
	public Set<String> getCategories() {
		return categories;
	}

	/**
	 * @return the people
	 */
	public Set<Person> getPeople() {
		return people;
	}

	/**
	 * @return the video
	 */
	public Video getVideo() {
		return video;
	}

	/**
	 * @return true if the video being edited is new
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	/**
	 * @param people
	 *            the people to set
	 */
	public void setPeople(Set<Person> people) {
		this.people = people;
	}

	/**
	 * @param video
	 *            the video to set
	 */
	public void setVideo(Video video) {
		this.video = video;
	}

}

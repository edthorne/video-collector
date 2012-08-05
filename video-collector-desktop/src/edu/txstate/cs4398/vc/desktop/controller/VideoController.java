/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.controller;

import edu.txstate.cs4398.vc.desktop.model.VideoModel;
import edu.txstate.cs4398.vc.desktop.view.VideoView;
import edu.txstate.cs4398.vc.model.Collection;
import edu.txstate.cs4398.vc.model.Video;

/**
 * @author Ed
 * 
 */
public class VideoController extends AbstractController {
	private Collection collection;

	/**
	 * Constructs a new video editor for the given video.
	 * 
	 * @param video
	 *            the video to edit or null for a new video
	 * @param collection
	 *            the collection the video belongs to
	 */
	public VideoController(Video video, Collection collection) {
		this.collection = collection;
		boolean isNew = (video == null);
		VideoModel model = new VideoModel(isNew);
		model.setCategories(collection.getCategories());
		model.setPeople(collection.getPeople());
		model.setVideo(isNew ? new Video() : video);
		setModel(model);
		setView(new VideoView(getModel(), this));
		getView().setVisible(true);
	}

	/**
	 * Closes the view.
	 */
	public void close() {
		getView().dispose();
	}

	@Override
	public VideoModel getModel() {
		return (VideoModel) super.getModel();
	}

	@Override
	public VideoView getView() {
		return (VideoView) super.getView();
	}

	/**
	 * Saves the current field values
	 */
	public void save() {
		VideoView view = getView();

		// update model video with field values
		Video video = getModel().getVideo();
		video.setCategory(view.getCategory());
		video.setDirector(view.getDirector());
		video.setMyRating(view.getMyRating());
		video.setNotes(view.getNotes());
		video.setRated(view.getRated());
		video.setRuntime(view.getRuntime());
		video.setTitle(view.getTitle());
		video.setUpc(view.getUpc());
		video.setYear(view.getYear());

		// if it's a new video
		if (getModel().isNew()) {
			// add it to the collection
			collection.addVideo(getModel().getVideo());
		}

		// finally, close the view
		close();
	}
}

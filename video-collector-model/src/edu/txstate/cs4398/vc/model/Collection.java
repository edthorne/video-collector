package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import edu.txstate.cs4398.vc.model.Video;
import edu.txstate.cs4398.vc.model.Category;

public class Collection extends AbstractModel {
	private String name;
	private ArrayList<Video> videos = new ArrayList<Video>();
	private ArrayList<Category> categories = new ArrayList<Category>();

	public Collection(String name) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public void addVideo(Video video) {
		throw new UnsupportedOperationException();
	}

	public void addCategory(Category category) {
		throw new UnsupportedOperationException();
	}
}
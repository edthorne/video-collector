package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import edu.txstate.cs4398.vc.model.Video;

public class Category extends AbstractModel {
	private int categoryId;
	private String name;
	private ArrayList<Video> videos = new ArrayList<Video>();

	public Category(int categoryId, String name) {
		throw new UnsupportedOperationException();
	}

	public int getCategoryId() {
		throw new UnsupportedOperationException();
	}

	public void setCategoryId(int categoryId) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}
}
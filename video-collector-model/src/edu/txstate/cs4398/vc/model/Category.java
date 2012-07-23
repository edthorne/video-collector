package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.txstate.cs4398.vc.model.Video;

@XmlRootElement(name = "category")

public class Category extends AbstractModel {
	private int categoryId;
	private String name;
	private ArrayList<Video> videos = new ArrayList<Video>();

	public Category()
	{
		
	}
	
	public Category(int categoryId, String name) {
		this.categoryId = categoryId;
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
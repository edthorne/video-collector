package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;
import edu.txstate.cs4398.vc.model.Video;

public class Person extends AbstractModel {
	private int personId;
	private String lastName;
	private String firstName;
	private ArrayList<Video> directed = new ArrayList<Video>();

	public Person(int personId, String lastName, String firstName) {
		throw new UnsupportedOperationException();
	}

	public int getPersonId() {
		throw new UnsupportedOperationException();
	}

	public void setPersonId(int personId) {
		throw new UnsupportedOperationException();
	}

	public String getLastName() {
		throw new UnsupportedOperationException();
	}

	public void setLastName(String lastName) {
		throw new UnsupportedOperationException();
	}

	public String getFirstName() {
		throw new UnsupportedOperationException();
	}

	public void setFirstName(String firstName) {
		throw new UnsupportedOperationException();
	}
}
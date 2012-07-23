package edu.txstate.cs4398.vc.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.txstate.cs4398.vc.model.Video;

@XmlRootElement(name = "person")
@XmlType(propOrder = {"personId", "lastName", "firstName"})

public class Person extends AbstractModel {
	private int personId;
	private String lastName;
	private String firstName;
	private ArrayList<Video> directed = new ArrayList<Video>();

	public Person(){}
	
	public Person(int personId, String lastName, String firstName) {
		this.personId = personId;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
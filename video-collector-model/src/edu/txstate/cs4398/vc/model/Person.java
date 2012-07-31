package edu.txstate.cs4398.vc.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a person outside of the <code>Collection</code> for easy
 * marshaling and client use.
 * 
 * @author Ed
 */
@XmlRootElement(name = "person")
@XmlType(propOrder = { "lastName", "firstName" })
public class Person extends AbstractModel {
	/**
	 * Event identifier for property changes.
	 */
	public static final int PROPERTY_CHANGED = 1;

	private String lastName;
	private String firstName;

	/**
	 * Creates a new Person. You must set a last name before marshaling the
	 * person.
	 */
	public Person() {
		// default constructor
	}

	/**
	 * Creates a new person with the given last and first names.
	 * 
	 * @param lastName
	 *            the last name to set
	 * @param firstName
	 *            the first name to set
	 */
	public Person(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}

	/**
	 * @return the last name of the person
	 */
	@XmlElement(required = true)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the person.
	 * 
	 * @param lastName
	 *            the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "lastName"));
	}

	/**
	 * @return the first name of the person
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the person.
	 * 
	 * @param firstName
	 *            the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "firstName"));
	}
}
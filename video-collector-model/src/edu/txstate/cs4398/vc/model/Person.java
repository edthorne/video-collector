package edu.txstate.cs4398.vc.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Represents a person outside of the <code>Collection</code> for easy
 * marshaling and client use.
 * 
 * @author Ed
 */
@Root
public class Person {
	@Element( required = true )
	private String lastName;
	@Element
	private String firstName;

	/**
	 * Creates a new Person. Useful only for JAXB marshalling.
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
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the first name of the person
	 */
	public String getFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(lastName);
		if (firstName != null) {
			builder.append(", ").append(firstName);
		}
		return builder.toString();
	}
}

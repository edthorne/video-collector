package edu.txstate.cs4398.vc.model;

import java.text.ParseException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Represents a person outside of the <code>Collection</code> for easy
 * marshaling and client use.
 * 
 * @author Ed
 */
@Root
@XmlRootElement(name = "person")
@XmlType(propOrder = { "lastName", "firstName" })
public class Person {
	@Element(required = true)
	@XmlElement(required = true)
	private String lastName;
	@Element
	@XmlElement
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/**
	 * Parses a string to create a Person. Supported formats are:
	 * <ul>
	 * <li>Last, First</li>
	 * <li>First Last</li>
	 * <li>Last</li>
	 * </ul>
	 * 
	 * @param value
	 *            the value to parse
	 * @return a Person with the given name
	 * @throws ParseException
	 *             if the text doesn't conform to a supported format
	 */
	public static Person fromString(String value) throws ParseException {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		if (value.trim().length() == 0) {
			throw new ParseException("Empty string not parsable", 0);
		}
		String[] parts;
		boolean hasComma = value.contains(",");
		if (hasComma) {
			parts = value.split(",");
		} else {
			// greedy space matching
			parts = value.split(" +");
		}
		String last, first;
		switch (parts.length) {
		case 0:
			throw new ParseException("Empty string not parsable", 0);
		case 1:
			last = parts[0].trim();
			return new Person(last, null);
		case 2:
			if (hasComma) {
				last = parts[0].trim();
				first = parts[1].trim();
			} else {
				last = parts[1].trim();
				first = parts[0].trim();
			}
			return new Person(last, first);
		default:
			// too many tokens
			int errorOffset;
			char token;
			if (hasComma) {
				token = ',';
			} else {
				token = ' ';
			}
			errorOffset = value.indexOf(token, parts[0].length() + parts[1].length());
			throw new ParseException("Too many tokens to parse", errorOffset);
		}
	}
}

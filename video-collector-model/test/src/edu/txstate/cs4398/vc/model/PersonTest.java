package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	private static final String FIRST_NAME = "Sean";
	private static final String LAST_NAME = "Connery";

	private Person person1, person2;

	@Before
	public void setUp() throws Exception {
		person1 = new Person();
		person2 = new Person(LAST_NAME, FIRST_NAME);
	}

	@Test
	public void testConstructors() {
		// default constructor (from setUp)
		assertNull(person1.getLastName());
		assertNull(person1.getFirstName());
		// name constructor
		assertEquals(LAST_NAME, person2.getLastName());
		assertEquals(FIRST_NAME, person2.getFirstName());
	}

	@Test
	public void testEqualsHashCode() {
		assertFalse(person1.equals(person2));
		assertFalse(person1.hashCode() == person2.hashCode());
		assertFalse(person2.equals(person1));
		assertFalse(person1.hashCode() == person2.hashCode());
		person1 = new Person(LAST_NAME, FIRST_NAME);
		assertTrue(person1.equals(person2));
		assertTrue(person2.equals(person1));
		assertEquals(person1, person2);
		assertTrue(person1.hashCode() == person2.hashCode());
	}
}

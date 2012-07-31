package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	private static final String FIRST_NAME = "Sean";
	private static final String LAST_NAME = "Connery";

	private Person person;
	private MockListener listener;

	@Before
	public void setUp() throws Exception {
		person = new Person();
		listener = new MockListener();
		person.addModelListener(listener);
	}

	@Test
	public void testConstructors() {
		// default constructor (from setUp)
		assertNull(person.getLastName());
		assertNull(person.getFirstName());
		// name constructor
		person = new Person(LAST_NAME, FIRST_NAME);
		assertEquals(LAST_NAME, person.getLastName());
		assertEquals(FIRST_NAME, person.getFirstName());
	}

	@Test
	public void testSimpleProperties() {
		assertNull(person.getLastName());
		assertNull(person.getFirstName());
		assertFalse(listener.containsEvent(person, Person.PROPERTY_CHANGED));
		person.setLastName(LAST_NAME);
		person.setFirstName(FIRST_NAME);
		assertEquals(LAST_NAME, person.getLastName());
		assertEquals(FIRST_NAME, person.getFirstName());
		assertTrue(listener.containsEvent(person, Person.PROPERTY_CHANGED));
		assertEquals(2, listener.countEvents(person, Person.PROPERTY_CHANGED));
	}
}

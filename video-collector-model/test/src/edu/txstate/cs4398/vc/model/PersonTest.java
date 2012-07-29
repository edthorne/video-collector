package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	private static final String FIRST_NAME = "Sean";
	private static final String LAST_NAME = "Connery";
	private static final String TITLE1 = "Highlander";
	private static final String TITLE2 = "Presidio";

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
		assertEquals("",person.getPersonId());
		assertEquals(0, person.getDirectedVideos().size());
		// name constructor
		person = new Person(LAST_NAME, FIRST_NAME);
		assertEquals(LAST_NAME, person.getLastName());
		assertEquals(FIRST_NAME, person.getFirstName());
		assertEquals("",person.getPersonId());
		assertEquals(0, person.getDirectedVideos().size());
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

	@Test
	public void testPersonId() {
		assertEquals("",person.getPersonId());
		UUID id = UUID.randomUUID();
		person.setPersonId(id.toString());
		assertEquals(id.toString(), person.getPersonId());
	}

	@Test
	public void testDirected() {
		// starts empty
		assertEquals(0, person.getDirectedVideos().size());
		assertFalse(listener.containsEvent(person, Person.VIDEO_ADDED));
		assertFalse(listener.containsEvent(person, Person.VIDEO_REMOVED));
		// add video
		final Video VIDEO1 = new Video(TITLE1);
		person.addDirectedVideo(VIDEO1);
		assertEquals(1, person.getDirectedVideos().size());
		assertEquals(person, VIDEO1.getDirector());
		assertTrue(listener.containsEvent(person, Person.VIDEO_ADDED));
		listener.reset();
		// add another
		final Video VIDEO2 = new Video(TITLE2);
		person.addDirectedVideo(VIDEO2);
		assertEquals(2, person.getDirectedVideos().size());
		assertEquals(person, VIDEO2.getDirector());
		assertTrue(listener.containsEvent(person, Person.VIDEO_ADDED));
		// get the list
		List<Video> directed = person.getDirectedVideos();
		assertEquals(2, directed.size());
		Video video = directed.get(0);
		// try to modify the list
		try {
			directed.remove(video);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, directed.size());
			assertFalse(listener.containsEvent(person, Person.VIDEO_REMOVED));
		}
		// remove video via person
		person.removeDirectedVideo(video);
		assertEquals(1, directed.size());
		assertEquals(null, video.getDirector());
		assertFalse(directed.contains(VIDEO1));
		assertTrue(directed.contains(VIDEO2));
		assertTrue(listener.containsEvent(person, Person.VIDEO_REMOVED));
	}
}

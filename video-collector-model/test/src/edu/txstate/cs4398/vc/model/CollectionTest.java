/**
 * 
 */
package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test fixture for testing basic collection properties and operations.
 * 
 * @author Ed
 */
@Ignore
public class CollectionTest {
	private static final String COLLECTION_NAME = "Collection name";
	private static final String CATEGORY1_NAME = "Action";
	private static final String CATEGORY2_NAME = "Drama";
	private static final String DIRECTOR1_LAST = "McTiernan";
	private static final String DIRECTOR1_FIRST = "John";
	private static final String DIRECTOR2_LAST = "Cameron";
	private static final String DIRECTOR2_FIRST = "James";
	private static final String VIDEO1_TITLE = "Die Hard";
	private static final String VIDEO2_TITLE = "Titanic";
	private static final String VIDEO3_TITLE = "Avatar";

	private Collection collection;
	private MockListener listener;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		collection = new Collection();
		listener = new MockListener();
		collection.addModelListener(listener);
	}

	@Test
	public void testConstructors() {
		// default constructor
		assertNull(collection.getName());
		assertEquals(0, collection.getCategories().size());
		assertEquals(0, collection.getPeople().size());
		assertEquals(0, collection.getVideos().size());
		collection = new Collection(COLLECTION_NAME);
		assertEquals(COLLECTION_NAME, collection.getName());
		assertEquals(0, collection.getCategories().size());
		assertEquals(0, collection.getPeople().size());
		assertEquals(0, collection.getVideos().size());
	}

	@Test
	public void testName() {
		assertNull(collection.getName());
		assertFalse(listener.containsEvent(collection,
				Collection.PROPERTY_CHANGED));
		collection.setName(COLLECTION_NAME);
		assertEquals(COLLECTION_NAME, collection.getName());
		assertTrue(listener.containsEvent(collection,
				Collection.PROPERTY_CHANGED));
	}

	@Test
	public void testCategories() {
		// starts empty
		assertEquals(0, collection.getCategories().size());
		assertFalse(listener.containsEvent(collection,
				Collection.CATEGORY_ADDED));
		assertFalse(listener.containsEvent(collection,
				Collection.CATEGORY_REMOVED));
		// add category via video add
		final Video video1 = new Video(VIDEO1_TITLE);
		video1.setCategory(CATEGORY1_NAME);
		collection.addVideo(video1);
		assertEquals(1, collection.getCategories().size());
		assertTrue(listener
				.containsEvent(collection, Collection.CATEGORY_ADDED));
		listener.reset();
		// add another
		final Video video2 = new Video(VIDEO2_TITLE);
		video2.setCategory(CATEGORY2_NAME);
		collection.addVideo(video2);
		assertEquals(2, collection.getCategories().size());
		assertTrue(listener
				.containsEvent(collection, Collection.CATEGORY_ADDED));
		listener.reset();
		// add a duplicate (same category name)
		final Video video3 = new Video(VIDEO3_TITLE);
		video3.setCategory(CATEGORY1_NAME);
		collection.addVideo(video3);
		assertEquals(2, collection.getCategories().size());
		assertFalse(listener.containsEvent(collection,
				Collection.CATEGORY_ADDED));
		// get the set
		Set<String> categories = collection.getCategories();
		assertEquals(2, categories.size());
		// try to modify the list
		try {
			categories.remove(CATEGORY2_NAME);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, categories.size());
		}
		// remove category by removing the last video
		collection.removeVideo(video2);
		assertEquals(1, categories.size());
		assertTrue(categories.contains(CATEGORY1_NAME));
		assertFalse(categories.contains(CATEGORY2_NAME));
		assertTrue(listener.containsEvent(collection,
				Collection.CATEGORY_REMOVED));
	}

	@Test
	public void testPeople() {
		// starts empty
		assertEquals(0, collection.getPeople().size());
		assertFalse(listener.containsEvent(collection, Collection.PERSON_ADDED));
		assertFalse(listener.containsEvent(collection,
				Collection.PERSON_REMOVED));
		// add person
		final Person person1 = new Person(DIRECTOR1_LAST, DIRECTOR1_FIRST);
		Person person = collection.addPerson(person1);
		assertNotSame(person1, person); // not same object
		assertEquals(person1.getLastName(), person.getLastName());
		assertEquals(person1.getFirstName(), person.getFirstName());
		assertEquals(1, collection.getPeople().size());
		assertTrue(listener.containsEvent(collection, Collection.PERSON_ADDED));
		listener.reset();
		// add another
		final Person person2 = new Person(DIRECTOR2_LAST, DIRECTOR2_FIRST);
		person = collection.addPerson(person2);
		assertNotSame(person2, person); // not same object
		assertEquals(2, collection.getPeople().size());
		assertTrue(listener.containsEvent(collection, Collection.PERSON_ADDED));
		listener.reset();
		// add a duplicate (same last, first name)
		person = collection.addPerson(new Person(DIRECTOR1_LAST,
				DIRECTOR1_FIRST));
		assertNotSame(person1, person); // not same object
		assertEquals(2, collection.getPeople().size());
		assertFalse(listener.containsEvent(collection, Collection.PERSON_ADDED));
		// get the list
		List<? extends Person> people = collection.getPeople();
		assertEquals(2, people.size());
		person = people.get(0);
		// try to modify the list
		try {
			people.remove(person);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, people.size());
		}
		// add a video to the category/collection
		final Video VIDEO1 = new Video(VIDEO1_TITLE);
		VIDEO1.setDirector(person);
		assertEquals(person, VIDEO1.getDirector());
		// remove person
		collection.removePerson(person);
		assertEquals(1, people.size());
		assertFalse(people.contains(person1));
		assertTrue(people.contains(person2));
		assertTrue(listener
				.containsEvent(collection, Collection.PERSON_REMOVED));
		// director should be nullified
		assertNull(VIDEO1.getDirector());
	}

	@Test
	public void testVideos() {
		// starts empty
		assertEquals(0, collection.getVideos().size());
		assertFalse(listener.containsEvent(collection, Collection.VIDEO_ADDED));
		assertFalse(listener
				.containsEvent(collection, Collection.VIDEO_REMOVED));
		// add video
		// add another with category and director
		// add a duplicate (same video title)
		// get the list
		// try to modify the list
		// remove video
	}
}

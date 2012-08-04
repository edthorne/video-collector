/**
 * 
 */
package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * A test fixture for testing basic collection properties and operations.
 * 
 * @author Ed
 */
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
		// add category
		collection.addCategory(CATEGORY1_NAME);
		assertEquals(1, collection.getCategories().size());
		assertTrue(listener
				.containsEvent(collection, Collection.CATEGORY_ADDED));
		listener.reset();
		// add another
		collection.addCategory(CATEGORY2_NAME);
		assertEquals(2, collection.getCategories().size());
		assertTrue(listener
				.containsEvent(collection, Collection.CATEGORY_ADDED));
		listener.reset();
		// add a duplicate (same category name)
		collection.addCategory(CATEGORY1_NAME);
		assertEquals(2, collection.getCategories().size());
		assertFalse(listener.containsEvent(collection,
				Collection.CATEGORY_ADDED));
		// get the set
		Set<String> categories = collection.getCategories();
		assertEquals(2, categories.size());
		// try to modify the list
		try {
			categories.remove(CATEGORY1_NAME);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, categories.size());
		}
		// remove existing category
		collection.removeCategory(CATEGORY2_NAME);
		assertEquals(1, categories.size());
		assertTrue(categories.contains(CATEGORY1_NAME));
		assertFalse(categories.contains(CATEGORY2_NAME));
		assertTrue(listener.containsEvent(collection,
				Collection.CATEGORY_REMOVED));
		listener.reset();
		// remove it again
		collection.removeCategory(CATEGORY2_NAME);
		assertEquals(1, categories.size());
		assertTrue(categories.contains(CATEGORY1_NAME));
		assertFalse(categories.contains(CATEGORY2_NAME));
		assertFalse(listener.containsEvent(collection,
				Collection.CATEGORY_REMOVED));
		listener.reset();
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
		collection.addPerson(person1);
		assertEquals(1, collection.getPeople().size());
		assertTrue(listener.containsEvent(collection, Collection.PERSON_ADDED));
		listener.reset();
		// add another
		final Person person2 = new Person(DIRECTOR2_LAST, DIRECTOR2_FIRST);
		collection.addPerson(person2);
		assertEquals(2, collection.getPeople().size());
		assertTrue(listener.containsEvent(collection, Collection.PERSON_ADDED));
		listener.reset();
		// add a duplicate (same last, first name)
		collection.addPerson(new Person(DIRECTOR1_LAST,
				DIRECTOR1_FIRST));
		assertEquals(2, collection.getPeople().size());
		assertFalse(listener.containsEvent(collection, Collection.PERSON_ADDED));
		// get the list
		Set<Person> people = collection.getPeople();
		assertEquals(2, people.size());
		// try to modify the list
		try {
			people.remove(person1);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, people.size());
		}
		// remove person
		collection.removePerson(person2);
		assertEquals(1, people.size());
		assertTrue(people.contains(person1));
		assertFalse(people.contains(person2));
		assertTrue(listener
				.containsEvent(collection, Collection.PERSON_REMOVED));
		listener.reset();
		// remove person again
		collection.removePerson(person2);
		assertEquals(1, people.size());
		assertTrue(people.contains(person1));
		assertFalse(people.contains(person2));
		assertFalse(listener
				.containsEvent(collection, Collection.PERSON_REMOVED));
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

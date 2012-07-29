/**
 * 
 */
package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.util.List;

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
		assertFalse(listener.containsEvent(collection, Collection.PROPERTY_CHANGED));
		collection.setName(COLLECTION_NAME);
		assertEquals(COLLECTION_NAME, collection.getName());
		assertTrue(listener.containsEvent(collection, Collection.PROPERTY_CHANGED));
	}

	@Test
	public void testCategories() {
		// starts empty
		assertEquals(0, collection.getCategories().size());
		assertFalse(listener.containsEvent(collection, Collection.CATEGORY_ADDED));
		assertFalse(listener.containsEvent(collection, Collection.CATEGORY_REMOVED));
		// add category
		final Category CATEGORY1 = new Category(CATEGORY1_NAME);
		assertEquals("",CATEGORY1.getCategoryId()); // blank id
		Category category = collection.addCategory(CATEGORY1);
		assertEquals(CATEGORY1, category); // same object
		assertNotNull(category.getCategoryId()); // id set
		assertEquals(1, collection.getCategories().size());
		assertTrue(listener.containsEvent(collection, Collection.CATEGORY_ADDED));
		listener.reset();
		// add another
		final Category CATEGORY2 = new Category(CATEGORY2_NAME);
		assertEquals("",CATEGORY2.getCategoryId()); // blank id
		category = collection.addCategory(CATEGORY2);
		assertEquals(CATEGORY2, category); // same object
		assertNotNull(category.getCategoryId()); // id set
		assertEquals(2, collection.getCategories().size());
		assertTrue(listener.containsEvent(collection, Collection.CATEGORY_ADDED));
		listener.reset();
		// add a duplicate (same category name)
		category = collection.addCategory(new Category(CATEGORY1_NAME));
		assertEquals(CATEGORY1, category); // same object
		assertNotNull(category.getCategoryId()); // id set
		assertEquals(2, collection.getCategories().size());
		assertFalse(listener.containsEvent(collection, Collection.CATEGORY_ADDED));
		// get the list
		List<Category> categories = collection.getCategories();
		assertEquals(2, categories.size());
		category = categories.get(0);
		// try to modify the list
		try {
			categories.remove(category);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, categories.size());
		}
		// add a video to the category/collection
		final Video VIDEO1 = new Video(VIDEO1_TITLE);
		VIDEO1.setCategory(category);
		assertEquals(category, VIDEO1.getCategory());
		// remove category
		collection.removeCategory(category);
		assertEquals(1, categories.size());
		assertFalse(categories.contains(CATEGORY1));
		assertTrue(categories.contains(CATEGORY2));
		assertTrue(listener.containsEvent(collection, Collection.CATEGORY_REMOVED));
		// category should be nullified
		assertNull(VIDEO1.getCategory());
	}

	@Test
	public void testPeople() {
		// starts empty
		assertEquals(0, collection.getPeople().size());
		assertFalse(listener.containsEvent(collection, Collection.PERSON_ADDED));
		assertFalse(listener.containsEvent(collection, Collection.PERSON_REMOVED));
		// add person
		final Person PERSON1 = new Person(DIRECTOR1_LAST, DIRECTOR1_FIRST);
		assertEquals("",PERSON1.getPersonId()); // blank id
		Person person = collection.addPerson(PERSON1);
		assertEquals(PERSON1, person); // same object
		assertNotNull(person.getPersonId()); // id set
		assertEquals(1, collection.getPeople().size());
		assertTrue(listener.containsEvent(collection, Collection.PERSON_ADDED));
		listener.reset();
		// add another
		final Person PERSON2 = new Person(DIRECTOR2_LAST, DIRECTOR2_FIRST);
		assertEquals("",PERSON2.getPersonId()); // blank id
		person = collection.addPerson(PERSON2);
		assertEquals(PERSON2, person); // same object
		assertNotNull(person.getPersonId()); // id set
		assertEquals(2, collection.getPeople().size());
		assertTrue(listener.containsEvent(collection, Collection.PERSON_ADDED));
		listener.reset();
		// add a duplicate (same last, first name)
		person = collection.addPerson(new Person(DIRECTOR1_LAST, DIRECTOR1_FIRST));
		assertEquals(PERSON1, person); // same object
		assertNotNull(person.getPersonId()); // id set
		assertEquals(2, collection.getPeople().size());
		assertFalse(listener.containsEvent(collection, Collection.PERSON_ADDED));
		// get the list
		List<Person> people = collection.getPeople();
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
		// remove category
		collection.removePerson(person);
		assertEquals(1, people.size());
		assertFalse(people.contains(PERSON1));
		assertTrue(people.contains(PERSON2));
		assertTrue(listener.containsEvent(collection, Collection.PERSON_REMOVED));
		// director should be nullified
		assertNull(VIDEO1.getDirector());
	}

	@Test
	public void testVideos() {
		// starts empty
		assertEquals(0, collection.getVideos().size());
		assertFalse(listener.containsEvent(collection, Collection.VIDEO_ADDED));
		assertFalse(listener.containsEvent(collection, Collection.VIDEO_REMOVED));
		// add video
		final Video VIDEO1 = new Video(VIDEO1_TITLE);
		assertEquals("",VIDEO1.getVideoId()); // blank id
		Video video = collection.addVideo(VIDEO1);
		assertEquals(VIDEO1, video); // same object
		assertNotNull(video.getVideoId()); // id set
		assertEquals(1, collection.getVideos().size());
		assertTrue(listener.containsEvent(collection, Collection.VIDEO_ADDED));
		listener.reset();
		// add another with category and director
		final Video VIDEO2 = new Video(VIDEO2_TITLE);
		assertEquals("",VIDEO2.getVideoId()); // blank id
		VIDEO2.setCategory(new Category(CATEGORY2_NAME));
		assertNotNull(VIDEO2.getCategory());
		assertEquals("",VIDEO2.getCategory().getCategoryId());
		VIDEO2.setDirector(new Person(DIRECTOR2_LAST, DIRECTOR2_FIRST));
		assertNotNull(VIDEO2.getDirector());
		assertEquals("",VIDEO2.getDirector().getPersonId());
		video = collection.addVideo(VIDEO2);
		assertEquals(VIDEO2, video); // same object
		assertNotNull(video.getVideoId()); // id set
		assertNotNull(video.getCategory().getCategoryId()); // id set on add
		assertNotNull(video.getDirector().getPersonId()); // id set on add
		assertEquals(2, collection.getVideos().size());
		assertTrue(listener.containsEvent(collection, Collection.VIDEO_ADDED));
		listener.reset();
		// add a duplicate (same video title)
		video = collection.addVideo(new Video(VIDEO1_TITLE));
		assertEquals(VIDEO1, video); // same object
		assertNotNull(video.getVideoId()); // id set
		assertEquals(2, collection.getVideos().size());
		assertFalse(listener.containsEvent(collection, Collection.VIDEO_ADDED));
		// get the list
		List<Video> videos = collection.getVideos();
		assertEquals(2, videos.size());
		video = videos.get(0);
		// try to modify the list
		try {
			videos.remove(video);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, videos.size());
		}
		// add a category and director to the first video
		final Category CATEGORY = new Category(CATEGORY1_NAME);
		assertEquals("",CATEGORY.getCategoryId());
		video.setCategory(CATEGORY);
		assertEquals(CATEGORY, video.getCategory());
		assertNotNull(video.getCategory().getCategoryId()); // category id added by collection listener
		final Person DIRECTOR = new Person(DIRECTOR1_LAST, DIRECTOR1_FIRST);
		assertEquals("",DIRECTOR.getPersonId());
		video.setDirector(DIRECTOR);
		assertEquals(DIRECTOR, video.getDirector());
		assertNotNull(video.getDirector().getPersonId()); // person id added by collection listener
		assertTrue(CATEGORY.getVideos().contains(video));
		assertTrue(DIRECTOR.getDirectedVideos().contains(video));
		// remove video
		collection.removeVideo(video);
		assertEquals(1, videos.size());
		assertFalse(videos.contains(VIDEO1));
		assertTrue(videos.contains(VIDEO2));
		assertTrue(listener.containsEvent(collection, Collection.VIDEO_REMOVED));
		// category and director should be nullified
		assertNull(VIDEO1.getCategory());
		assertNull(VIDEO1.getDirector());
		// category and director should no longer contain the video
		assertFalse(CATEGORY.getVideos().contains(video));
		assertFalse(DIRECTOR.getDirectedVideos().contains(video));
	}
}

package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
	private static final String NAME = "Drama";
	private static final String TITLE1 = "Highlander";
	private static final String TITLE2 = "Presidio";
	private Category category;
	private MockListener listener;

	@Before
	public void setUp() throws Exception {
		category = new Category();
		listener = new MockListener();
		category.addModelListener(listener);
	}

	@Test
	public void testConstructors() {
		// default constructor (from setUp)
		assertNull(category.getName());
		assertNull(category.getCategoryId());
		assertEquals(0, category.getVideos().size());
		// name constructor
		category = new Category(NAME);
		assertEquals(NAME, category.getName());
		assertNull(category.getCategoryId());
		assertEquals(0, category.getVideos().size());
	}

	@Test
	public void testSimpleProperties() {
		assertNull(category.getName());
		assertFalse(listener.containsEvent(category, Category.PROPERTY_CHANGED));
		category.setName(NAME);
		assertEquals(NAME, category.getName());
		assertTrue(listener.containsEvent(category, Category.PROPERTY_CHANGED));
	}

	@Test
	public void testCategoryId() {
		assertNull(category.getCategoryId());
		UUID id = UUID.randomUUID();
		category.setCategoryId(id.toString());
		assertEquals(id.toString(), category.getCategoryId());
	}

	@Test
	public void testVideos() {
		// starts empty
		assertEquals(0, category.getVideos().size());
		assertFalse(listener.containsEvent(category, Category.VIDEO_ADDED));
		assertFalse(listener.containsEvent(category, Category.VIDEO_REMOVED));
		// add video
		final Video VIDEO1 = new Video(TITLE1);
		category.addVideo(VIDEO1);
		assertEquals(1, category.getVideos().size());
		assertEquals(category, VIDEO1.getCategory());
		assertTrue(listener.containsEvent(category, Category.VIDEO_ADDED));
		listener.reset();
		// add another
		final Video VIDEO2 = new Video(TITLE2);
		category.addVideo(VIDEO2);
		assertEquals(2, category.getVideos().size());
		assertEquals(category, VIDEO2.getCategory());
		assertTrue(listener.containsEvent(category, Category.VIDEO_ADDED));
		// get the list
		List<Video> videos = category.getVideos();
		assertEquals(2, videos.size());
		Video video = videos.get(0);
		// try to modify the list
		try {
			videos.remove(video);
			fail("Should have thrown exception");
		} catch (UnsupportedOperationException uoe) {
			assertEquals(2, videos.size());
			assertFalse(listener.containsEvent(category, Category.VIDEO_REMOVED));
		}
		// remove video via category
		category.removeVideo(video);
		assertEquals(1, videos.size());
		assertEquals(null, video.getCategory());
		assertFalse(videos.contains(VIDEO1));
		assertTrue(videos.contains(VIDEO2));
		assertTrue(listener.containsEvent(category, Category.VIDEO_REMOVED));

	}
}

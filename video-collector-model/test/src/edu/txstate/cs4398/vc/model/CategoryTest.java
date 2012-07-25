package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
	private static final String NAME = "Drama";
	private static final String TITLE1 = "Highlander";
	private static final String TITLE2 = "Presidio";
	private Category category;

	@Before
	public void setUp() throws Exception {
		category = new Category(NAME);
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
		category.setName(NAME);
		assertEquals(NAME, category.getName());
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
		// add video
		category.addVideo(new Video(TITLE1));
		assertEquals(1, category.getVideos().size());
		// add another
		category.addVideo(new Video(TITLE2));
		assertEquals(2, category.getVideos().size());
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
		}
		// remove video via category
		category.removeVideo(video);
		assertEquals(1, videos.size());
	}
}

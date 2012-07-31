package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VideoTest {
	private static final String CATEGORY = "Comedy";
	private static final String TITLE = "Ferris Bueller's Day Off";
	private static final int YEAR = 2001;
	private static final int RUNTIME = 118;
	private static final String UPC = "024532159786";
	private static final String NOTES = "My movie notes";
	private static final String FIRST_NAME = "John";
	private static final String LAST_NAME = "Hughes";

	private Video video;
	private MockListener listener;

	@Before
	public void setUp() throws Exception {
		video = new Video();
		listener = new MockListener();
		video.addModelListener(listener);
	}

	@Test
	public void testConstructors() {
		// default constructor (from setUp)
		assertNull(video.getTitle());
		assertEquals(0, video.getYear());
		assertEquals(0, video.getRuntime());
		assertNull(video.getUpc());
		assertEquals(0, video.getMyRating());
		assertNull(video.getNotes());
		assertNull(video.getDirector());
		assertNull(video.getRated());
		assertNull(video.getCategory());
		// title constructor
		video = new Video(TITLE);
		assertEquals(TITLE, video.getTitle());
		assertEquals(0, video.getYear());
		assertEquals(0, video.getRuntime());
		assertNull(video.getUpc());
		assertEquals(0, video.getMyRating());
		assertNull(video.getNotes());
		assertNull(video.getDirector());
		assertNull(video.getRated());
		assertNull(video.getCategory());
	}

	@Test
	public void testSimpleProperties() {
		assertNull(video.getTitle());
		assertEquals(0, video.getYear());
		assertEquals(0, video.getRuntime());
		assertNull(video.getUpc());
		assertNull(video.getNotes());
		assertNull(video.getRated());
		assertNull(video.getCategory());
		assertFalse(listener.containsEvent(video, Video.PROPERTY_CHANGED));

		video.setTitle(TITLE);
		video.setYear(YEAR);
		video.setRuntime(RUNTIME);
		video.setUpc(UPC);
		video.setNotes(NOTES);
		final Rating RATED = Rating.PG13;
		video.setRated(RATED);
		video.setCategory(CATEGORY);

		assertEquals(TITLE, video.getTitle());
		assertEquals(YEAR, video.getYear());
		assertEquals(RUNTIME, video.getRuntime());
		assertEquals(UPC, video.getUpc());
		assertEquals(NOTES, video.getNotes());
		assertEquals(RATED, video.getRated());
		assertTrue(listener.containsEvent(video, Video.PROPERTY_CHANGED));
		assertEquals(7, listener.countEvents(video, Video.PROPERTY_CHANGED));
	}

	@Test
	public void testMyRating() {
		assertEquals(0, video.getMyRating());
		assertFalse(listener.containsEvent(video, Video.PROPERTY_CHANGED));
		video.setMyRating((byte) 3);
		assertEquals(3, video.getMyRating());
		assertTrue(listener.containsEvent(video, Video.PROPERTY_CHANGED));
		// valid values 0-5
		try {
			video.setMyRating((byte) 6);
			fail("6 is not a valid value");
		} catch (IllegalArgumentException iae) {
			// should still be 3
			assertEquals(3, video.getMyRating());
		}
		try {
			video.setMyRating((byte) -6);
			fail("-6 is not a valid value");
		} catch (IllegalArgumentException iae) {
			// should still be 3
			assertEquals(3, video.getMyRating());
		}
		video.setMyRating((byte) 5);
		assertEquals(5, video.getMyRating());
		video.setMyRating((byte) 0);
		assertEquals(0, video.getMyRating());
	}

	@Test
	public void testDirector() {
		assertNull(video.getDirector());
		assertFalse(listener.containsEvent(video, Video.PROPERTY_CHANGED));
		final Person DIRECTOR = new Person(LAST_NAME, FIRST_NAME);
		video.setDirector(DIRECTOR);
		assertEquals(DIRECTOR, video.getDirector());
		assertTrue(listener.containsEvent(video, Video.PROPERTY_CHANGED));
		listener.reset();
		video.setDirector(null);
		assertEquals(null, video.getDirector());
		assertTrue(listener.containsEvent(video, Video.PROPERTY_CHANGED));
	}
}

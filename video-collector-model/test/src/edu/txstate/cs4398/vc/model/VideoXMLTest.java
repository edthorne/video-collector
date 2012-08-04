/**
 * 
 */
package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * A test fixture for testing video XML marshalling and unmarshalling.
 * 
 * @author Ed
 */
public class VideoXMLTest {
	// test data
	private static final String MANN_NAME = "Mann";
	private static final String MICHAEL_NAME = "Michael";

	private static final String HEAT_CATEGORY = "Action";
	private static final String HEAT_NOTES = "Best action movie of all time!";
	private static final Rating HEAT_RATED = Rating.R;
	private static final byte HEAT_RATING = (byte) 5;
	private static final int HEAT_RUNTIME = 172;
	private static final String HEAT_TITLE = "Heat";
	private static final String HEAT_UPC = "085391419228";
	private static final int HEAT_YEAR = 1995;

	private static Serializer serializer;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;

	private Video video;
	private File xmlFile;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// set up simple XML serializer
		serializer = new Persister();
		// set up JAXB marshallers
		JAXBContext context = JAXBContext.newInstance(Video.class);
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		unmarshaller = context.createUnmarshaller();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		xmlFile = File.createTempFile("Video", "xml");

		// build a video
		video = new Video(HEAT_TITLE);
		video.setCategory(HEAT_CATEGORY);
		video.setDirector(new Person(MANN_NAME, MICHAEL_NAME));
		video.setMyRating(HEAT_RATING);
		video.setNotes(HEAT_NOTES);
		video.setRated(HEAT_RATED);
		video.setRuntime(HEAT_RUNTIME);
		video.setUpc(HEAT_UPC);
		video.setYear(HEAT_YEAR);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		xmlFile.deleteOnExit();
	}

	@Test
	public void testJAXBMarshalling() throws Exception {
		// marshal the video to a temp file
		marshaller.marshal(video, xmlFile);
		marshaller.marshal(video, System.out);

		// unmarshal the collection from the file
		Video fVideo = (Video) unmarshaller.unmarshal(xmlFile);

		// compare the created video with the file video
		Person michaelMann = fVideo.getDirector();
		assertEquals(MANN_NAME, michaelMann.getLastName());
		assertEquals(MICHAEL_NAME, michaelMann.getFirstName());

		assertEquals(HEAT_TITLE, fVideo.getTitle());
		assertEquals(HEAT_CATEGORY, fVideo.getCategory());
		assertEquals(HEAT_NOTES, fVideo.getNotes());
		assertEquals(HEAT_RATED, fVideo.getRated());
		assertEquals(HEAT_RATING, fVideo.getMyRating());
		assertEquals(HEAT_RUNTIME, fVideo.getRuntime());
		assertEquals(HEAT_UPC, fVideo.getUpc());
		assertEquals(HEAT_YEAR, fVideo.getYear());
	}

	@Test
	public void testSimpleMarshalling() throws Exception {
		// write the video to a temp file
		serializer.write(video, xmlFile);
		serializer.write(video, System.out);

		// read the collection from the file
		Video fVideo = (Video) serializer.read(Video.class, xmlFile);

		// compare the created video with the file video
		Person michaelMann = fVideo.getDirector();
		assertEquals(MANN_NAME, michaelMann.getLastName());
		assertEquals(MICHAEL_NAME, michaelMann.getFirstName());

		assertEquals(HEAT_TITLE, fVideo.getTitle());
		assertEquals(HEAT_CATEGORY, fVideo.getCategory());
		assertEquals(HEAT_NOTES, fVideo.getNotes());
		assertEquals(HEAT_RATED, fVideo.getRated());
		assertEquals(HEAT_RATING, fVideo.getMyRating());
		assertEquals(HEAT_RUNTIME, fVideo.getRuntime());
		assertEquals(HEAT_UPC, fVideo.getUpc());
		assertEquals(HEAT_YEAR, fVideo.getYear());
	}

	@Test
	public void testSimpleNullMarshalling() throws Exception {
		video.setDirector(null);
		video.setCategory(null);
		
		// write the video to a temp file
		serializer.write(video, xmlFile);
		serializer.write(video, System.out);

		// read the collection from the file
		Video fVideo = (Video) serializer.read(Video.class, xmlFile);

		// compare the created video with the file video
		assertNull(fVideo.getDirector());
		assertEquals(HEAT_TITLE, fVideo.getTitle());
		assertNull(fVideo.getCategory());
		assertEquals(HEAT_NOTES, fVideo.getNotes());
		assertEquals(HEAT_RATED, fVideo.getRated());
		assertEquals(HEAT_RATING, fVideo.getMyRating());
		assertEquals(HEAT_RUNTIME, fVideo.getRuntime());
		assertEquals(HEAT_UPC, fVideo.getUpc());
		assertEquals(HEAT_YEAR, fVideo.getYear());
	}
}

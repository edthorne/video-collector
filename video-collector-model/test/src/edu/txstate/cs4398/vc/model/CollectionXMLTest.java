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
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test fixture for testing collection XML marshalling and unmarshalling.
 * 
 * @author Ed
 */
@Ignore
public class CollectionXMLTest {
	// test data
	private static final String ACTION_CATEGORY = "Action";
	private static final String THRILLER_CATEGORY = "Thriller";

	private static final String MANN_NAME = "Mann";
	private static final String MICHAEL_NAME = "Michael";
	private static final String PETERSEN_NAME = "Petersen";
	private static final String WOLFGANG_NAME = "Wolfgang";

	private static final Rating AF_ONE_RATED = Rating.R;
	private static final byte AF_ONE_RATING = (byte) 3;
	private static final int AF_ONE_RUNTIME = 125;
	private static final String AF_ONE_TITLE = "Air Force One";
	private static final String AF_ONE_UPC = "043396718890";
	private static final int AF_ONE_YEAR = 1997;

	private static final String COLLATERAL_NOTES = "Nobody captures L.A. at night like Michael Mann";
	private static final Rating COLLATERAL_RATED = Rating.R;
	private static final byte COLLATERAL_RATING = (byte) 4;
	private static final int COLLATERAL_RUNTIME = 120;
	private static final String COLLATERAL_TITLE = "Collateral";
	private static final String COLLATERAL_UPC = "678149173420";
	private static final int COLLATERAL_YEAR = 2004;
	private static final String COLLECTION_NAME = "Test Collection";

	private static final String HEAT_NOTES = "Best action movie of all time!";
	private static final Rating HEAT_RATED = Rating.R;
	private static final byte HEAT_RATING = (byte) 5;
	private static final int HEAT_RUNTIME = 172;
	private static final String HEAT_TITLE = "Heat";
	private static final String HEAT_UPC = "085391419228";
	private static final int HEAT_YEAR = 1995;

	private static final String MIAMI_VICE_NOTES = "Nice reboot with excellent music";
	private static final Rating MIAMI_VICE_RATED = Rating.UNRATED;
	private static final byte MIAMI_VICE_RATING = (byte) 3;
	private static final int MIAMI_VICE_RUNTIME = 140;
	private static final String MIAMI_VICE_TITLE = "Miami Vice, Unrated Director's Edition";
	private static final String MIAMI_VICE_UPC = "025193326621";
	private static final int MIAMI_VICE_YEAR = 2006;

	private static final String PERF_STORM_NOTES = "Three words: Air Force Rescue";
	private static final Rating PERF_STORM_RATED = Rating.PG13;
	private static final byte PERF_STORM_RATING = (byte) 4;
	private static final int PERF_STORM_RUNTIME = 130;
	private static final String PERF_STORM_TITLE = "The Perfect Storm";
	private static final String PERF_STORM_UPC = "085391858423";
	private static final int PERF_STORM_YEAR = 2000;

	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	private Collection collection;
	private File xmlFile;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// set up JAXB marshallers
		JAXBContext context = JAXBContext.newInstance(Collection.class);
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		unmarshaller = context.createUnmarshaller();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		xmlFile = File.createTempFile("Collection", "xml");
		collection = new Collection(COLLECTION_NAME);

		// build a collection
		Person michaelMann = new Person(MANN_NAME, MICHAEL_NAME);
		Video video = new Video(HEAT_TITLE);
		video.setCategory(ACTION_CATEGORY);
		video.setDirector(michaelMann);
		video.setMyRating(HEAT_RATING);
		video.setNotes(HEAT_NOTES);
		video.setRated(HEAT_RATED);
		video.setRuntime(HEAT_RUNTIME);
		video.setUpc(HEAT_UPC);
		video.setYear(HEAT_YEAR);
		collection.addVideo(video);

		video = new Video(COLLATERAL_TITLE);
		video.setCategory(THRILLER_CATEGORY);
		video.setDirector(michaelMann);
		video.setMyRating(COLLATERAL_RATING);
		video.setNotes(COLLATERAL_NOTES);
		video.setRated(COLLATERAL_RATED);
		video.setRuntime(COLLATERAL_RUNTIME);
		video.setUpc(COLLATERAL_UPC);
		video.setYear(COLLATERAL_YEAR);
		collection.addVideo(video);

		video = new Video(MIAMI_VICE_TITLE);
		video.setCategory(ACTION_CATEGORY);
		video.setDirector(michaelMann);
		video.setMyRating(MIAMI_VICE_RATING);
		video.setNotes(MIAMI_VICE_NOTES);
		video.setRated(MIAMI_VICE_RATED);
		video.setRuntime(MIAMI_VICE_RUNTIME);
		video.setUpc(MIAMI_VICE_UPC);
		video.setYear(MIAMI_VICE_YEAR);
		collection.addVideo(video);

		video = new Video(PERF_STORM_TITLE);
		video.setCategory(ACTION_CATEGORY);
		Person wolfgangPetersen = new Person(PETERSEN_NAME, WOLFGANG_NAME);
		video.setDirector(wolfgangPetersen);
		video.setMyRating(PERF_STORM_RATING);
		video.setNotes(PERF_STORM_NOTES);
		video.setRated(PERF_STORM_RATED);
		video.setRuntime(PERF_STORM_RUNTIME);
		video.setUpc(PERF_STORM_UPC);
		video.setYear(PERF_STORM_YEAR);
		collection.addVideo(video);

		video = new Video(AF_ONE_TITLE);
		video.setCategory(ACTION_CATEGORY);
		video.setDirector(wolfgangPetersen);
		video.setMyRating(AF_ONE_RATING);
		video.setRated(AF_ONE_RATED);
		video.setRuntime(AF_ONE_RUNTIME);
		video.setUpc(AF_ONE_UPC);
		video.setYear(AF_ONE_YEAR);
		collection.addVideo(video);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		xmlFile.deleteOnExit();
	}

	@Test
	public void testXMLMarshalling() throws Exception {
		// marshal the collection to a temp file
		marshaller.marshal(collection, xmlFile);
		marshaller.marshal(collection, System.out);

		// unmarshal the collection from the file
		Collection fCollection = (Collection) unmarshaller.unmarshal(xmlFile);

		// compare the created collection with the file collection
//		assertEquals(2, fCollection.getCategories().size());
		assertEquals(2, fCollection.getPeople().size());
		assertEquals(5, fCollection.getVideos().size());

		Person michaelMann = fCollection.getPeople().get(0);
		assertEquals(MANN_NAME, michaelMann.getLastName());
		assertEquals(MICHAEL_NAME, michaelMann.getFirstName());
		Person wolfgangPetersen = fCollection.getPeople().get(1);
		assertEquals(PETERSEN_NAME, wolfgangPetersen.getLastName());
		assertEquals(WOLFGANG_NAME, wolfgangPetersen.getFirstName());

		Video heat = fCollection.getVideos().get(0);
		assertEquals(HEAT_TITLE, heat.getTitle());
		assertEquals(HEAT_NOTES, heat.getNotes());
		assertEquals(HEAT_RATED, heat.getRated());
		assertEquals(HEAT_RATING, heat.getMyRating());
		assertEquals(HEAT_RUNTIME, heat.getRuntime());
		assertEquals(HEAT_UPC, heat.getUpc());
		assertEquals(HEAT_YEAR, heat.getYear());
		assertEquals(michaelMann, heat.getDirector());
		assertEquals(ACTION_CATEGORY, heat.getCategory());

		Video collateral = fCollection.getVideos().get(1);
		assertEquals(COLLATERAL_TITLE, collateral.getTitle());
		assertEquals(COLLATERAL_NOTES, collateral.getNotes());
		assertEquals(COLLATERAL_RATED, collateral.getRated());
		assertEquals(COLLATERAL_RATING, collateral.getMyRating());
		assertEquals(COLLATERAL_RUNTIME, collateral.getRuntime());
		assertEquals(COLLATERAL_UPC, collateral.getUpc());
		assertEquals(COLLATERAL_YEAR, collateral.getYear());
		assertEquals(michaelMann, collateral.getDirector());
		assertEquals(THRILLER_CATEGORY, collateral.getCategory());

		Video miamiVice = fCollection.getVideos().get(2);
		assertEquals(MIAMI_VICE_TITLE, miamiVice.getTitle());
		assertEquals(MIAMI_VICE_NOTES, miamiVice.getNotes());
		assertEquals(MIAMI_VICE_RATED, miamiVice.getRated());
		assertEquals(MIAMI_VICE_RATING, miamiVice.getMyRating());
		assertEquals(MIAMI_VICE_RUNTIME, miamiVice.getRuntime());
		assertEquals(MIAMI_VICE_UPC, miamiVice.getUpc());
		assertEquals(MIAMI_VICE_YEAR, miamiVice.getYear());
		assertEquals(michaelMann, miamiVice.getDirector());
		assertEquals(ACTION_CATEGORY, miamiVice.getCategory());

		Video perfStorm = fCollection.getVideos().get(3);
		assertEquals(PERF_STORM_TITLE, perfStorm.getTitle());
		assertEquals(PERF_STORM_NOTES, perfStorm.getNotes());
		assertEquals(PERF_STORM_RATED, perfStorm.getRated());
		assertEquals(PERF_STORM_RATING, perfStorm.getMyRating());
		assertEquals(PERF_STORM_RUNTIME, perfStorm.getRuntime());
		assertEquals(PERF_STORM_UPC, perfStorm.getUpc());
		assertEquals(PERF_STORM_YEAR, perfStorm.getYear());
		assertEquals(wolfgangPetersen, perfStorm.getDirector());
		assertEquals(ACTION_CATEGORY, perfStorm.getCategory());

		Video airForce1 = fCollection.getVideos().get(4);
		assertEquals(AF_ONE_TITLE, airForce1.getTitle());
		assertEquals(AF_ONE_RATED, airForce1.getRated());
		assertEquals(AF_ONE_RATING, airForce1.getMyRating());
		assertEquals(AF_ONE_RUNTIME, airForce1.getRuntime());
		assertEquals(AF_ONE_UPC, airForce1.getUpc());
		assertEquals(AF_ONE_YEAR, airForce1.getYear());
		assertEquals(wolfgangPetersen, airForce1.getDirector());
		assertEquals(ACTION_CATEGORY, airForce1.getCategory());
	}
}

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test fixture for testing collection XML marshalling and unmarshalling.
 * 
 * @author Ed
 */
public class CollectionXMLTest {
	private static final String COLLECTION_NAME = "Collection name";
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
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		xmlFile = File.createTempFile("Collection", "xml");
		System.out.println(xmlFile.toString());
		collection = new Collection();

		// build a collection

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
		fail("Not implemented");
	}
}

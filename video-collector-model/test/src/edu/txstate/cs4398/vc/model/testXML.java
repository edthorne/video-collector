package edu.txstate.cs4398.vc.model;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testXML {

	private static final String XML_FILE = "./video-jaxb.xml";
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws JAXBException, IOException {
		Collection videos = new Collection();
		for(int i = 1; i <= 10; i++)
		{
			Person person = new Person(i, "Last"+i, "First"+i);
			Category category = new Category(i%5,"Category"+(i%5));
			Video video = new Video(i, "Video"+i, 1994+i, 80+5*i, 123456789012L+i, person, Rating.R, category);
			videos.addCategory(category);
			videos.addPerson(person);
			videos.addVideo(video);
		}
		
		JAXBContext context = JAXBContext.newInstance(Collection.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(videos, System.out);

		Writer w = null;
		try {
			w = new FileWriter(XML_FILE);
			m.marshal(videos, w);
		} finally {
			try {
				w.close();
			} catch (Exception e) {
			}
		}
		
		// get variables from our xml file, created before
		System.out.println();
		System.out.println("Output from our XML File: ");
		Unmarshaller um = context.createUnmarshaller();
		Collection videos2 = (Collection) um.unmarshal(new FileReader(XML_FILE));

		for(Video video : videos2.getVideos())
		{
			System.out.println(video.getTitle());
		}


	}

}

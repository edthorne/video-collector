package edu.txstate.cs4398.vc.desktop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.txstate.cs4398.vc.desktop.services.VideoLookupService;
import edu.txstate.cs4398.vc.model.Video;

/**
 * 
 * @author mnosler
 *
 */
public class TestVideoLookupService {

	private final String UPC_HIMYM = "024543382034";
	private final String UPC_DUKES = "012569736658";
	private final String UPC_SEABISCUIT = "025192328824";
	private final String UPC_HANGOVER = "883929057832";
	private final String UPC_JOHNNY = "014381126327";
	private final String UPC_COLDMTN = "786936242164";
	private final String UPC_ALAMO = "786936242126";
	
	
	VideoLookupService videoLookupService;
	@Before
	public void setUp() throws Exception {
		Properties testProps = new Properties();
		testProps.load(new FileInputStream("config/token.properties"));
		String tomato_token = testProps.getProperty("tomato_api_token");
		String access_token = testProps.getProperty("upc_api_token");

		videoLookupService = new VideoLookupService(access_token, tomato_token);
		System.out.println("------------------------------");
	}

	@After
	public void tearDown() throws Exception {
		videoLookupService = null;
	}

	@Test
	public void testUPCLookup() throws Exception {
		String upcResponse = null;
		
		upcResponse = videoLookupService.getProductName(UPC_DUKES);
		
		System.out.println("Response from upc Service: " + upcResponse);

		assertEquals(upcResponse,"The Dukes of Hazzard (Unrated Widescreen Edition)");
	}
	
	@Test
	public void testDVDLookup() throws Exception {
		System.out.println("Name lookup : The Dukes of Hazzard (Unrated Widescreen Edition)");
		Video video = new Video();
		
		video = videoLookupService.getVideoByName("The Dukes of Hazzard (Unrated Widescreen Edition)",video);
		System.out.println("Title: " + video.getTitle());
		System.out.println("Runtime: " + video.getRuntime());
		
		assertEquals(video.getTitle(),"The Dukes of Hazzard");
		assertEquals(video.getRuntime(), 104);
	}
	
	@Test
	public void testTVLookup() throws Exception {
		System.out.println("Test upc and Video lookup with TV Series barcode");
		String videoName = null;;
		
		videoName = videoLookupService.getProductName(UPC_HIMYM);
		
		Video video = new Video();
		
		video = videoLookupService.getVideoByName(videoName, video);
		
		assertEquals(video.getTitle(),"How I Met Your Mother");
		
	}
	
	@Test
	public void testList() throws Exception {
		ArrayList<String> upcList = new ArrayList<String>();
		ArrayList<Video> videos = new ArrayList<Video>();
		upcList.add(UPC_HIMYM);
		upcList.add(UPC_DUKES);
		upcList.add(UPC_SEABISCUIT);
		upcList.add(UPC_HANGOVER);
		upcList.add(UPC_JOHNNY);
		upcList.add(UPC_COLDMTN);
		upcList.add(UPC_ALAMO);

		for(String upc : upcList)
		{
			System.out.println("UPC: " + upc);
			Video video = new Video();
			video.setUpc(upc);
			video = videoLookupService.getVideoByName(videoLookupService.getProductName(upc), video);
			printVideo(video);
			videos.add(video);
		}
	}
	
	@Test
	public void testInvalidBarcode()
	{
		System.out.println("Test invalid barcode");
		String videoName = null;
		try {
			videoName = videoLookupService.getProductName("junk");
		} catch (IOException e) {
			fail();
		} catch (JSONException e) {
			// result not found, OK.
			System.out.println("no result");
			return;
		}
		System.out.println("video" + videoName);
		fail();
	}
	
	@Test
	public void testInvalidMovie() throws Exception
	{
		System.out.println("Test invalid movie");
		Video video = new Video();
		video = videoLookupService.getVideoByName("invalidmoviesearchstring", video);
		
		assertNull(video.getTitle());
	}
	
	@Test
	public void testImdbRuntime()
	{
		int runtime = videoLookupService.getImdbRuntime("3 h 14 min");
		assertEquals(runtime, 194);
		
		runtime = videoLookupService.getImdbRuntime("14 min");
		assertEquals(runtime, 14);
	}
	
	private void printVideo(Video video)
	{
		System.out.println("Video:");
		System.out.println("Title: " + video.getTitle());
		System.out.println("Year:  " + video.getYear());
		System.out.println("Runtime: " + video.getRuntime());
		System.out.println("UPC: " + video.getUpc());
		if(video.getDirector() != null)
			System.out.println("Director: " + video.getDirector().getFirstName() + " " + video.getDirector().getLastName());
		else
			System.out.println("Director: N/A");
		System.out.println("Rated: " + video.getRated());
		System.out.println("ImgURL: " + video.getImageURL());
		System.out.println();
	}
}

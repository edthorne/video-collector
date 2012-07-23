package edu.txstate.cs4398.vc.desktop.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.txstate.cs4398.vc.desktop.services.VideoLookupService;
import edu.txstate.cs4398.vc.model.Category;
import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Rating;
import edu.txstate.cs4398.vc.model.Video;

/**
 * 
 * @author mnosler
 *
 */
public class TestVideoLookupService {

	VideoLookupService videoLookupService;
	@Before
	public void setUp() throws Exception {
		videoLookupService = new VideoLookupService();
		System.out.println("------------------------------");
	}

	@After
	public void tearDown() throws Exception {
		videoLookupService = null;
	}

	@Test
	public void testUPCLookup() {
		System.out.println("UPC 012569736658");
		String upcResponse = null;
		try {
			upcResponse = videoLookupService.getProductName("012569736658");
			System.out.println("Response from upc Service: " + upcResponse);
		} catch (IOException e) {
			fail();
		} catch (JSONException e) {
			fail();
		}
	    assertEquals(upcResponse,"The Dukes of Hazzard (Unrated Widescreen Edition)");
	}
	
	@Test
	public void testDVDLookup() {
		System.out.println("Name lookup : The Dukes of Hazzard (Unrated Widescreen Edition)");
		Video video = new Video(0,"");
		try {
			video = videoLookupService.getVideoByName("The Dukes of Hazzard (Unrated Widescreen Edition)",video);
			System.out.println("Title: " + video.getTitle());
			System.out.println("Runtime: " + video.getRuntime());
		} catch (IOException e) {
			fail();
		} catch (JSONException e) {
			fail();
		}
		
		assertEquals(video.getTitle(),"The Dukes of Hazzard");
		assertEquals(video.getRuntime(), 104);
	}
	
	@Test
	public void testTVLookup(){
		System.out.println("Test upc and Video lookup with TV Series barcode");
		System.out.println("UPC 024543382034");
		String videoName = null;;
		try {
			videoName = videoLookupService.getProductName("024543382034");
		} catch (IOException e) {
			fail();
		} catch (JSONException e) {
			fail();
		}
		
		Video video = new Video(0,"");
		
		try {
			video = videoLookupService.getVideoByName(videoName, video);
		} catch (IOException e) {
			fail();
		} catch (JSONException e) {
			fail();
		}
		
		assertEquals(video.getTitle(),"How I Met Your Mother");
		
	}
	
	@Test
	public void testList(){
		ArrayList<String> upcList = new ArrayList<String>();
		ArrayList<Video> videos = new ArrayList<Video>();
		upcList.add("024543382034");
		upcList.add("012569736658");
		upcList.add("025192328824");
		upcList.add("883929057832");
		upcList.add("014381126327");
		upcList.add("786936242164");
		upcList.add("786936242126");
		
		for(String upc : upcList)
		{
			System.out.println("UPC: " + upc);
			try {
				Video video = new Video(upcList.indexOf(upc), "");
				video.setUpc(Long.parseLong(upc));
				video = videoLookupService.getVideoByName(videoLookupService.getProductName(upc), video);
				printVideo(video);
				videos.add(video);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
			}
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
	public void testInvalidMovie()
	{
		System.out.println("Test invalid movie");
		Video video = new Video(0,"nothinginthistitle");
		Video video2 = null;
		try {
			video = videoLookupService.getVideoByName("invalidmoviesearchstring", video);
			video2 = videoLookupService.getVideoByName("invalidmoviesearchstring", video);
		} catch (IOException e) {
			fail();
		} catch (JSONException e) {
			fail();
		}
		assertEquals(video, video2);
		assertEquals(video.getTitle(),"nothinginthistitle");
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
		System.out.println("Director: " + video.getDirector().getFirstName() + " " + video.getDirector().getLastName());
		System.out.println("Rated: " + video.getRated());
		System.out.println();
	}
}

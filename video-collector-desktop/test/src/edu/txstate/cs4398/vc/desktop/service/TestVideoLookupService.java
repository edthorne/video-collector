package edu.txstate.cs4398.vc.desktop.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.txstate.cs4398.vc.desktop.services.VideoLookupService;
import edu.txstate.cs4398.vc.model.Video;

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
			video = videoLookupService.getVideoByName("The Dukes of Hazzard (Unrated Widescreen Edition)");
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
			video = videoLookupService.getVideoByName(videoName);
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
				Video video = videoLookupService.getVideoByName(videoLookupService.getProductName(upc));
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

}

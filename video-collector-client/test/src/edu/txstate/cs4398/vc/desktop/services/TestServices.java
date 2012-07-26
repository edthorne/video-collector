package edu.txstate.cs4398.vc.desktop.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestServices {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		MobileServicesImplService service = new MobileServicesImplService();
		MobileServices services = service.getMobileServicesImplPort();
		String result = services.echo("Now is the time");
		System.out.println(result);
		fail("Not yet implemented");
	}

}

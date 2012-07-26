package edu.txstate.cs4398.vc.desktop.services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A class to system test the web services client.  The desktop
 * application must be running for the tests to succeed.
 * 
 * @author Ed
 */
public class TestServices {
	private MobileServices services;

	@Before
	public void setUp() throws Exception {
		// get the web service from the default wsdl URL (http://localhost:8796/MobileServices?wsdl)
		MobileServicesImplService service = new MobileServicesImplService();
		// get the services implementation from the web service port
		services = service.getMobileServicesImplPort();
	}

	@Test
	public void testEcho() {
		final String message = "Testing echo service.";
		String result = services.echo(message);
		assertEquals(message, result);
	}

}

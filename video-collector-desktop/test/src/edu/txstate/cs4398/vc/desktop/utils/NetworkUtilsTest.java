/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.utils;

import static org.junit.Assert.*;

import java.net.NetworkInterface;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Ed
 * 
 */
public class NetworkUtilsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInterfaces() throws Exception {
		/*
		 * since this will vary for each machine under test we have to make this
		 * generic enough to pass on any machine but still check the logic of
		 * the method
		 */
		List<NetworkInterface> interfaces = NetworkUtils.getInterfaces();
		assertTrue(interfaces.size() > 0);
		NetworkInterface netInt = interfaces.get(0);
		assertTrue(netInt.isUp());
		assertTrue(netInt.supportsMulticast());
	}

	@Test
	public void testGetIPAddress() {
		/*
		 * Because we cannot know the IP address of the system under test we can
		 * only check to see that an address is returned. This should work in
		 * all cases because each host should have a loopback adapter
		 * configured.
		 */
		String ipAddress = NetworkUtils.getIPAddress();
		assertNotNull(ipAddress);
	}

}

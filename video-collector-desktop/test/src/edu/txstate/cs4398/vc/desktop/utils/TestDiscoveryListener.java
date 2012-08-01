package edu.txstate.cs4398.vc.desktop.utils;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDiscoveryListener {
	private static final String WS_URI = "http://localhost:8796/MobileServices";
	DiscoveryListener listener;

	@Before
	public void setUp() throws Exception {
		listener = new DiscoveryListener();
		listener.setServiceAddress(WS_URI);
		listener.start();
	}

	@After
	public void tearDown() throws Exception {
		listener.setListening(false);
		listener.join(10 * 1000); // wait 10 seconds for shutdown
	}

	@Test
	public void test() throws Exception {
		// make sure the listener is up and running
		assertTrue(listener.isAlive());
		assertTrue(listener.isDaemon());
		Thread.sleep(50); // pause for listening to go true
		assertTrue(listener.isListening());

		// send the class name as the data, this should be a client identifier
		byte[] buf = TestDiscoveryListener.class.getName().getBytes("UTF-8");

		// send a multicast packet to the listener
		InetAddress group = InetAddress
				.getByName(DiscoveryListener.LISTEN_ADDRESS);
		MulticastSocket socket = new MulticastSocket();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, group,
				DiscoveryListener.LISTEN_PORT);
		socket.setTimeToLive(1); // only broadcast on the local subnet
		socket.send(packet);

		// wait for a response
		buf = new byte[256];
		packet = new DatagramPacket(buf, buf.length);
		socket.setSoTimeout(5000);// wait at most 5 seconds
		socket.receive(packet);

		// get the remote IP address from the response packet
		InetAddress serverAddress = packet.getAddress();
		assertNotNull(serverAddress.getHostAddress());

		// get the web service url from the response data
		String received = new String(packet.getData(), 0, packet.getLength(),
				"UTF-8");
		assertNotNull(received);
		assertEquals(WS_URI, received);
	}

}

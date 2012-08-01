/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

/**
 * This class listens on a multicast socket for clients searching for the
 * desktop application.
 * 
 * @author Ed
 */
public class DiscoveryListener extends Thread {
	/**
	 * The multicast address to listen on.
	 */
	static final String LISTEN_ADDRESS = "230.0.17.46";
	/**
	 * The listen port for the discovery listener.
	 */
	static final int LISTEN_PORT = 4398;
	/**
	 * How long, in milliseconds the listener should wait before checking to see
	 * if it should shut down.
	 */
	private static final int LISTEN_TIMEOUT = 5000;
	/**
	 * A boolean flag that indicates if the listener should keep listening.
	 */
	private boolean listening;
	/**
	 * The multicast socket to communicate with.
	 */
	private MulticastSocket socket;
	/**
	 * The multicast address in use by the listener.
	 */
	private InetAddress address;
	/**
	 * The web service address.
	 */
	private String serviceAddress;

	/**
	 * Creates a new listener thread.
	 */
	public DiscoveryListener() throws IOException {
		this("DiscoveryListener");
	}

	/**
	 * Creates a new listener thread with the given thread name.
	 * 
	 * @param name the thread name
	 */
	public DiscoveryListener(String name) throws IOException {
		super(name);
		this.setDaemon(true); // mark this thread as a daemon

		socket = new MulticastSocket(LISTEN_PORT);
		address = InetAddress.getByName(LISTEN_ADDRESS);
		socket.joinGroup(address);
		socket.setSoTimeout(LISTEN_TIMEOUT);
	}

	@Override
	public void run() {
		DatagramPacket packet;

		System.out.println("Starting discovery listener on port " + LISTEN_PORT);

		if (serviceAddress == null) {
			listening = false;
			System.err.println("Set service address before starting listener.");
		} else {
			listening = true;
		}

		while (listening) {
			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);

			try {
				// wait for client
				socket.receive(packet);
				// get client address and port
				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();
				// send client web service address
				buf = serviceAddress.getBytes("UTF-8");
				packet = new DatagramPacket(buf, buf.length,clientAddress,clientPort);
				packet.setAddress(clientAddress);
				socket.send(packet);
			} catch (SocketTimeoutException ste) {
				// expected after LISTEN_TIMEOUT, used to stop thread with
				// listening boolean variable
			} catch (IOException ioe) {
				// unexpected exception, stop listening
				ioe.printStackTrace();
				listening = false;
			}
		}
		System.out.println("Shutting down DiscoveryListener");
	}

	@Override
	protected void finalize() throws Throwable {
		// clean up
		socket.leaveGroup(address);
		socket.close();

		super.finalize();
	}

	/**
	 * @return the listening
	 */
	public boolean isListening() {
		return listening;
	}

	/**
	 * @param listening
	 *            the listening to set
	 */
	public void setListening(boolean listening) {
		this.listening = listening;
	}

	public void setServiceAddress(String wsURI) {
		this.serviceAddress = wsURI;
	}

}

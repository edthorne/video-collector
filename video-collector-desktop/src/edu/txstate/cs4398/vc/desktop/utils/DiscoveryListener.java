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
	private boolean listening = true;
	/**
	 * The multicast socket to communicate with.
	 */
	MulticastSocket socket;
	/**
	 * The multicast address in use by the listener.
	 */
	InetAddress address;

	/**
	 * Creates a new listener thread.
	 */
	public DiscoveryListener() throws IOException {
		this("DiscoveryListener");
	}

	/**
	 * @param name
	 */
	public DiscoveryListener(String name) throws IOException {
		super(name);
		this.setDaemon(true); // mark this thread as a daemon

		socket = new MulticastSocket(LISTEN_PORT);
		address = InetAddress.getByName(LISTEN_ADDRESS);
		socket.joinGroup(address);
		socket.setSoTimeout(LISTEN_TIMEOUT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		DatagramPacket packet;

		while (listening) {
			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);

			System.out.println("Listening on port " + LISTEN_PORT);
			try {
				// wait for client
				socket.receive(packet);
				// get data sent from client
				String received = new String(packet.getData(), 0,
						packet.getLength());
				System.out.println("received data: " + received);
				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();
				// send client web service address
				buf = "web service address".getBytes("UTF-8");
				packet = new DatagramPacket(buf, buf.length,clientAddress,clientPort);
				packet.setAddress(clientAddress);
				System.out.println("Sending: " + new String(buf));
				socket.send(packet);
			} catch (SocketTimeoutException ste) {
				// expected after LISTEN_TIMEOUT, used to stop thread with
				// listening
			} catch (IOException ioe) {
				// unexpected exception, stop listening
				ioe.printStackTrace();
				listening = false;
			}
		}
		System.out.println("Shutting down DiscoveryListener");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
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

}

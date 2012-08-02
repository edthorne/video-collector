package edu.txstate.cs4398.vc.mobile;

import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author Rudolph Newball
 * 
 * Searches for possible hosts listening to the MultiCast made by the android device.
 * It then returns the IP address of the first listener encountered.
 * If no connection is established it returns null.
 */

public class SearchTask extends BaseTask<TextView, TextView, String> {
	/**
	 * SearchTask constructor takes a listener and adds it to the EventHandler pool
	 * @param listener listener to be notified of any changes made
	 */
	public SearchTask(Listener listener){
		event = new EventHandler();
        event.addEventListener(listener);
        
	}

	@Override
	protected String doInBackground(TextView... text) {
		try {
			// Send the class name
			byte[] buf = SearchTask.class.getName().getBytes("UTF-8");

			// send a multicast packet to the listener
			InetAddress group = InetAddress.getByName("230.0.17.46");
			MulticastSocket socket = new MulticastSocket(4398);
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4398);
			socket.setTimeToLive(255); // On 255 it works
			socket.send(packet);

			// wait for a response
			buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			socket.setSoTimeout(5000);// wait at most 5 seconds
			publishProgress(text);
			Log.i("Interfaces", "Waiting for response");
		//	try{
				socket.receive(packet);
			//}catch(IOException e){
			//	socket.close();
			//	return null;
			//}
			InetAddress serverAddress = packet.getAddress();
			socket.close();
			
			return serverAddress.getHostAddress();
    	} catch (IOException e) {
    		
    		Log.i("Interfaces", "Exception occurred, server time out: " +e.getMessage());
		}
		return null;
	}
	@Override
	protected void onProgressUpdate(TextView... text){
		text[0].setText("Searching for host...");
	}

	@Override
	protected void onPostExecute(String address){
		TaskEvent<String> task = new TaskEvent<String>("SEARCH");
		if(address != null)
			task.setStatus(TaskEvent.Status.SUCCESS);
		else
			task.setStatus(TaskEvent.Status.FAIL);
		
		task.setResult(address);
	    event.notifyEvent(task);
	}

}
package edu.txstate.cs4398.vc.mobile;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MobileClient extends Activity implements View.OnClickListener{

	Button button;
	EditText textOut;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			        .detectDiskReads()
			        .detectDiskWrites()
			        .detectNetwork()   // or .detectAll() for all detectable problems
			        .penaltyLog()
			        .build());
    	
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_hello_world);
        
        button = (Button)this.findViewById(R.id.send);
        button.setOnClickListener(this);
        
        textOut = (EditText)this.findViewById(R.id.textout);
        
    }
    
    public void onClick(View v) {
    	System.out.println("In onClick dunction");
    	try {
			byte[] buf = textOut.getText().toString().getBytes("UTF-8");

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
			System.out.println("Waiting for response");
			socket.receive(packet);

			// get the remote IP address from the response packet
			InetAddress serverAddress = packet.getAddress();

			System.out.println("Server address: " + serverAddress.getHostAddress());

			// get the web service url from the response data
			String received = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
			System.out.println("Test data: " + received);
		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	textOut.setText("");
	}

}

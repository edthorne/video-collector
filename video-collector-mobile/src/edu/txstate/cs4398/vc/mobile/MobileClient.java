package edu.txstate.cs4398.vc.mobile;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.txstate.cs4398.vc.mobile.controller.CollectionReadWriter;
import edu.txstate.cs4398.vc.model.mobile.VideoMobile;


public class MobileClient extends Activity implements View.OnClickListener, Listener{

	private Button browse;
	private Button add;
	private EditText ipAddress;
	private VideoApp appState;
	private TextView progressText;
	private String serverAddress;
	private ProgressBar progressCircle;
	private Button sync;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.start_menu);
        
        progressCircle = (ProgressBar)this.findViewById(R.id.progress_cirlce);
        browse = (Button)this.findViewById(R.id.browse_button);
        browse.setOnClickListener(this);
        
        // add button will not be enable until there is a successful connection
        add = (Button)this.findViewById(R.id.add_button);
        add.setOnClickListener(this);
        add.setEnabled(false);
        
        sync = (Button)this.findViewById(R.id.sync_button);
        sync.setOnClickListener(this);
        
        progressText = (TextView)this.findViewById(R.id.status_text);
        appState = (VideoApp)this.getApplicationContext();
        
        Log.i("Interfaces", "Starting SearchTask...");
        SearchTask task = new SearchTask(this);
    	task.execute(progressText);
    }
    
    public void onClick(View v) {
		if(v.equals(browse)){
		
		}
		else if(v.equals(add)){
			Log.i("Interfaces", "Switching activity with address: " + serverAddress);
			appState.setWebServiceAddress(serverAddress);
			Intent next = new Intent(this, ScanActivity.class);
			this.startActivity(next);
			
    	}
		else if(v.equals(sync)) {
	        GetCollectionTask task = new GetCollectionTask(this);
	    	task.execute(serverAddress);
		}
	}
    /**
     * @param address String containing the IP address of the host.
     */
    public void onEvent(TaskEvent task) {
    	
    	TaskEvent.Status taskStatus = task.getStatus();
    	String action = task.getTask();
    	
    	if("SEARCH".equals(action)){
			if(taskStatus == TaskEvent.Status.SUCCESS){
				Log.i("Interfaces", "in onComplete with ip address: " + (String)task.getResult());	
				serverAddress = (String)task.getResult();
				connect();
				progressText.setText("Found host: "+ serverAddress +"\nAttempting connection...");
				
			}
			else{
				ipAddress = new EditText(this);
				Log.i("Interfaces", "Address is not valid");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Please enter your computer IP address:")
				       .setCancelable(false)
				       .setTitle("Cannot find host!")
				       .setView(ipAddress)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   Log.i("Interfaces", "Edit is: " + ipAddress.getText().toString());
				        	   serverAddress = ipAddress.getText().toString();
				        	   connect();
				        	   progressText.setText("Attempting connection...");
				           }
				       })
				       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    	   public void onClick(DialogInterface dialog, int id) {
				    		   progressCircle.setVisibility(View.INVISIBLE); // hide progress circle
				    		   progressText.setText("Connection Failed!");
				        	   dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
			
    	}
    	else if("CONNECT".equals(action)){
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			progressText.setText("Connection Succesful!");
    			progressCircle.setVisibility(View.INVISIBLE); // hide progress circle
    			add.setEnabled(true);		// enable add button
    		}
    	}
    	else if("GET_COLLECTION".equals(action)) {
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			List<VideoMobile> list = (List<VideoMobile>) task.getResult();
    			appState.setVideoList(list);
    			CollectionReadWriter rw = new CollectionReadWriter();
    			rw.writeVideosToXml(this);
    			Log.i("WRITTEN", "Videos were written to XML");
    			List<VideoMobile> vm = rw.getVideosFromXml(this);
    			for(VideoMobile vid : vm) {
    				Log.i("XML:", vid.getTitle());
    				
    			}
    				
    		}
    	}
		
    }
    
    private void connect(){
    	ConnectTask service = new ConnectTask(this);
		service.execute(serverAddress);
    }
    
    public void onStop() {
		CollectionReadWriter rw = new CollectionReadWriter();
		rw.writeVideosToXml(this);
    }

}

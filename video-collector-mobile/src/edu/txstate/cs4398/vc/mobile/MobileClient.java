package edu.txstate.cs4398.vc.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MobileClient extends Activity implements View.OnClickListener, Listener{

	private Button browse;
	private Button add;
	private EditText ipAddress;
	private VideoApp appState;
	private TextView progressText;
	private String serverAddress;
	private ProgressBar progressCircle;
	public static final String CONNECT_MESSAGE = "TESTING CONNECTION";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.start_menu);
        
        progressCircle = (ProgressBar)this.findViewById(R.id.progress_cirlce);
        browse = (Button)this.findViewById(R.id.browse_button);
        browse.setOnClickListener(this);
        
        // add button will not be enable until there is a successful connection
        add = (Button)this.findViewById(R.id.add_button);
        add.setOnClickListener(this);
        add.setEnabled(false);
        
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
	}
    /**
     * @param address String containing the IP address of the host.
     */
    public void onEvent(Task task) {
    	
    	String taskResult = task.getTaskResult();
    	String action = task.getTaskType();
    	Log.i("Interfaces", "Response: " + taskResult);
    	
    	if("SEARCH".equals(action)){
			if(taskResult != null){
				Log.i("Interfaces", "in onComplete with ip address: " + taskResult);	
				serverAddress = taskResult;
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
				                dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
			
    	}
    	else if("CONNECT".equals(action)){
    		if(CONNECT_MESSAGE.equals(taskResult)){
    			progressText.setText("Connection Succesful!");
    			progressCircle.setVisibility(View.INVISIBLE); // hide progress circle
    			add.setEnabled(true);		// enable add button
    		}
    	}
		
    }
    
    private void connect(){
    	ConnectTask service = new ConnectTask(this);
		service.execute(serverAddress);
    }

}

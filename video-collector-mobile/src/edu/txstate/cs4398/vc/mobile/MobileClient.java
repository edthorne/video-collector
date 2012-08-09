package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.txstate.cs4398.vc.mobile.controller.CollectionReadWriter;
import edu.txstate.cs4398.vc.mobile.video.VideoMobile;


public class MobileClient extends Activity implements View.OnClickListener, Listener{

	private Button browse;
	private Button add;
	private EditText ipAddress;
	private VideoApp appState;
	private TextView progressText;
	private String serverAddress;
	private ProgressBar progressCircle;
	private Button sync;
	private String status;
	private Button retry;
	private View custom;
	private DialogList current;
	private enum DialogList{
		FIRST, SECOND, THIRD, FOURTH
	}
	
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
        sync.setEnabled(false);
        
        retry = (Button)findViewById(R.id.retry_button);
        retry.setOnClickListener(this);
        
        progressText = (TextView)this.findViewById(R.id.status_text);
        appState = (VideoApp)this.getApplicationContext();
        CollectionReadWriter rw = new CollectionReadWriter();
        List<VideoMobile> xmlList = rw.getVideosFromXml(this);
        if (xmlList != null)
        	appState.setVideoList(xmlList);
        
        searchForHost();
        
    }
    
    private DialogList nextOnList(DialogList temp){
    	if(temp == DialogList.FIRST)
    		temp = DialogList.SECOND;
    	else if(temp == DialogList.SECOND)
    		temp = DialogList.THIRD;
    	else if(temp == DialogList.THIRD)
    		temp = DialogList.FOURTH;
    	else
    		temp = DialogList.FIRST;
    	
    	return temp;
    }
    
    private void searchForHost(){
    	progressCircle.setVisibility(View.VISIBLE);
    	retry.setVisibility(View.INVISIBLE);
    	SearchTask task = new SearchTask(this);
    	task.execute(progressText);
    }
    
    private void connectToHost(){
    	ConnectTask service = new ConnectTask(this);
		service.execute(serverAddress);
    }
    
    private boolean checkIpAddress(String ip){
    	if(ip.equals(""))
    		return false;
    	boolean pass = false, doubleDots = false;
    	char[] myString = ip.toCharArray();
    	int len = myString.length;
    	if(myString[0] != '.' && myString[len-1] != '.'){
    		int dot = 0, prevDot = -1;;
	    	for(int i = 0; i < len; i++){
	    		if(myString[i] == '.'){
	    			dot++;
	    			if(dot > 3 || prevDot == i-1){
	    	    		pass = false;
	    	    		doubleDots = true;
	    	    		break;
	    			}
	    			prevDot = i;
	    		}
	    	}
	    	if((dot > 0 && dot < 4) && !doubleDots)
	    		pass = true;
	    	else
	    		pass = false;
    	}
    	
    	return pass;
    }
    
    private void ipDialog(){
    	custom = getLayoutInflater().inflate(R.layout.dialog_layout, null);
    	
    	ipAddress = (EditText) custom.findViewById(R.id.ip_text);
    	SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
	    DialogList dl = DialogList.FIRST;
    	Spinner sp = (Spinner) custom.findViewById(R.id.ip_list);
    	ArrayList<String> sArr = new ArrayList<String>();
    	String s = settings.getString(dl.toString(), null);
    	while(s != null && dl != DialogList.FOURTH){
    		sArr.add(s);
    		dl = nextOnList(dl);
    		s = settings.getString(dl.toString(), null);
    	}
    	current = dl;
    		
    	ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sArr);
    	
    	adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	sp.setAdapter(adp);
    	sp.setPrompt("Select an IP:");
		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
				ipAddress.setText(parent.getSelectedItem().toString());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(status)
		       .setCancelable(false)
		       .setTitle("Could not find host!")
		       .setView(custom)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   boolean validIp = checkIpAddress(ipAddress.getText().toString());
		        	   if(validIp == true){
			        	   serverAddress = ipAddress.getText().toString();
			        	   progressText.setText("Attempting connection...");
			        	   connectToHost();
		        	   }
		        	   else{
		        		   if(!isFinishing()) {
			        		   status = "Incorrect IP Address, please try again:";
			        		   ipDialog();
		        		   }
		        	   }
		           }
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int id) {      
		    		   progressCircle.setVisibility(View.INVISIBLE); // hide progress circle
		    		   progressText.setText("Connection Failed!");
		    		   retry.setVisibility(View.VISIBLE);
		        	   dialog.cancel();
		           }
		       });
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
    
    public void onClick(View v) {
		if(v.equals(browse)){
			List<VideoMobile> vm = appState.getVideoList();
			Log.i("Interfaces", "In browse onClick");
			if(vm!=null && !vm.isEmpty()){
				Intent next = new Intent(this, BrowseActivity.class);
				this.startActivity(next);
			}
			else
				Toast.makeText(this.getApplicationContext(), "No entries, please Sync with desktop." , Toast.LENGTH_SHORT).show();
		}
		else if(v.equals(add)){
			appState.setWebServiceAddress(serverAddress);
			Intent next = new Intent(this, ScanActivity.class);
			this.startActivity(next);
			
    	}
		else if(v.equals(sync)) {
			GetCollectionTask task = new GetCollectionTask(this);
			task.execute(serverAddress);
		}
		else if(v.equals(retry)){
			searchForHost();
		}
	}  

    public void onEvent(TaskEvent task) {
    	
    	TaskEvent.Status taskStatus = task.getStatus();
    	String action = task.getTask();
    	
    	if("SEARCH".equals(action)){
			if(taskStatus == TaskEvent.Status.SUCCESS){
				serverAddress = (String)task.getResult();
				connectToHost();
				progressText.setText("Found host: "+ serverAddress +"\nAttempting connection...");
			}
			else{
				status = "Please enter your computer IP address: ";
				ipDialog();
			}
			
    	}
    	else if("CONNECT".equals(action)){
    		// On successful connection, save that address.
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			progressText.setText("Connection Succesful!");
    			progressCircle.setVisibility(View.INVISIBLE); // hide progress circle
    			add.setEnabled(true);		// enable add button
    			sync.setEnabled(true);
    			SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
	        	boolean shouldSave = true;
    			DialogList dl = DialogList.FIRST;
    	    	while(dl != DialogList.FOURTH){		
    	    		String s = settings.getString(dl.toString(), null);
    	    		if(serverAddress.equals(s)){		// break if we found the address is already in our list
    	    			shouldSave = false;
    	    			break;
    	    		}
    	    		dl = nextOnList(dl);
    	    	}
    			if(shouldSave && current != null){
	    			SharedPreferences.Editor editor = settings.edit();
		        	editor.putString(current.toString(), serverAddress);
		        	editor.commit();
		        	current = nextOnList(current);
    			}
    		}
    		else{
    			 progressCircle.setVisibility(View.INVISIBLE); // hide progress circle
	    		 progressText.setText("Connection Failed!");
	    		 retry.setVisibility(View.VISIBLE);
    		}
    	}
    	else if("GET_COLLECTION".equals(action)) {
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			List<VideoMobile> list = (List<VideoMobile>) task.getResult();
    			appState.setVideoList(list);
    			CollectionReadWriter rw = new CollectionReadWriter();
    			rw.writeVideosToXml(this);
    			Toast.makeText(this.getApplicationContext(), "Sync successful!" , Toast.LENGTH_SHORT).show();
    		}
    			
    	}
		
    }
        
    public void onStop() {
    	super.onStop();
		CollectionReadWriter rw = new CollectionReadWriter();
		rw.writeVideosToXml(this);
    }

}

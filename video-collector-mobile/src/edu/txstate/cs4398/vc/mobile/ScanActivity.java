package edu.txstate.cs4398.vc.mobile;

import edu.txstate.cs4398.vc.mobile.utils.IntentIntegrator;
import edu.txstate.cs4398.vc.mobile.utils.IntentResult;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.ksoap2.serialization.SoapObject;


public class ScanActivity extends Activity implements View.OnClickListener, Listener {
	Button scanButton;
	Button addVideoButton;
	EditText upcText;
	EditText videoTitle;
	EditText videoDirector;
	EditText videoYear;
	EditText videoRuntime;
	Spinner ratedSpinner;
	private String ipAddress;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        VideoApp appState = ((VideoApp)getApplicationContext());
        ipAddress = appState.getWebServiceAddress();

        scanButton = (Button)this.findViewById(R.id.btnScan);
        scanButton.setOnClickListener(this);
        addVideoButton = (Button)this.findViewById(R.id.btnAdd);
        addVideoButton.setOnClickListener(this);
        upcText = (EditText)this.findViewById(R.id.upcText);
        videoDirector = (EditText)this.findViewById(R.id.videoDirector);
        videoYear = (EditText)this.findViewById(R.id.videoYear);
        videoRuntime = (EditText)this.findViewById(R.id.videoRuntime);
        videoTitle = (EditText)this.findViewById(R.id.videoTitle);      
        ratedSpinner = (Spinner)this.findViewById(R.id.ratedSpinner);

    }

    
    public void onClick(View v) {
    	if(v.equals(scanButton))
    	{
    		System.out.println("In onClick dunction");
    		IntentIntegrator.initiateScan(this);
    	}
    	
    	if(v.equals(addVideoButton))
    	{
    		AddVideoTask addTask = new AddVideoTask(this);
    		addTask.execute(ipAddress, upcText.getText().toString(), videoTitle.getText().toString());
    	}

    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) 
    	{
    		case IntentIntegrator.REQUEST_CODE: {
    			if (resultCode != RESULT_CANCELED) {
    				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    				if (scanResult != null) {
    					String upc = scanResult.getContents();
    					
    					//put whatever you want to do with the code here
    					upcText.setText(upc);
    					
    					GetVideoTask getTask = new GetVideoTask(this);
    					getTask.execute(ipAddress, upc);
    			       
    				}
    			}
    		}
    	}
    }
    
    private void transformSoapResponse(SoapObject response) {
		String title = response.getPropertySafelyAsString("title");
		videoTitle.setText(title);
		SoapObject director = (SoapObject) response.getProperty("director");
		
		StringBuilder directorName = new StringBuilder();
		directorName.append(director.getPropertySafelyAsString("firstName"));
		directorName.append(" ");
		directorName.append(director.getPropertySafelyAsString("lastName"));
		videoDirector.setText(directorName.toString());
		
		String rated = response.getPropertySafelyAsString("rated");
		if(rated.equals("G"))
			ratedSpinner.setSelection(0);
		if(rated.equals("PG"))
			ratedSpinner.setSelection(1);
		if(rated.equals("PG13"))
			ratedSpinner.setSelection(2);
		if(rated.equals("R"))
			ratedSpinner.setSelection(3);
		if(rated.equals("NC17"))
			ratedSpinner.setSelection(4);
		if(rated.equals("NR"))
			ratedSpinner.setSelection(5);
		videoYear.setText(response.getPropertySafelyAsString("year"));
		videoRuntime.setText(response.getPropertySafelyAsString("runtime")); 
    }

	public void onEvent(TaskEvent task) {
		
		TaskEvent.Status taskStatus = task.getStatus();
    	String action = task.getTask();
    	
    	if("GET_VIDEO".equals(action)){
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			Log.i("Interfaces", "GET_VIDEO Success");
    			transformSoapResponse((SoapObject)task.getResult());
    		}
    	}
    	else if("ADD_VIDEO".equals(action)){
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			// Show toast notification
    	        Toast.makeText(this.getApplicationContext(), 
    	        		videoTitle.getText().toString() + " succesfully added to collection!", Toast.LENGTH_SHORT).show();
    		}
    		else
    		{
    			AlertDialog ad = new AlertDialog.Builder(this).create();  
    			ad.setCancelable(false); // This blocks the 'BACK' button  
    			ad.setMessage("Error sending video to server");  
    			ad.setButton("OK", new DialogInterface.OnClickListener() {  
    			    public void onClick(DialogInterface dialog, int which) {  
    			        dialog.dismiss();                      
    			    }  
    			});
    			ad.show();
    		}
    	}
    	
	}
}

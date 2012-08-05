package edu.txstate.cs4398.vc.mobile;

import edu.txstate.cs4398.vc.mobile.utils.IntentIntegrator;
import edu.txstate.cs4398.vc.mobile.utils.IntentResult;
import edu.txstate.cs4398.vc.model.mobile.VideoMobile;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
	ProgressBar progressCircle;
	VideoApp appState;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        appState = ((VideoApp)getApplicationContext());
        ipAddress = appState.getWebServiceAddress();

        scanButton = (Button)this.findViewById(R.id.btnScan);
        scanButton.setOnClickListener(this);
        addVideoButton = (Button)this.findViewById(R.id.btnAdd);
        addVideoButton.setOnClickListener(this);
        upcText = (EditText)this.findViewById(R.id.upcText);
        upcText.setInputType(InputType.TYPE_CLASS_NUMBER);
        videoDirector = (EditText)this.findViewById(R.id.videoDirector);
        videoYear = (EditText)this.findViewById(R.id.videoYear);
        videoYear.setInputType(InputType.TYPE_CLASS_NUMBER);
        videoRuntime = (EditText)this.findViewById(R.id.videoRuntime);
        videoRuntime.setInputType(InputType.TYPE_CLASS_NUMBER);
        videoTitle = (EditText)this.findViewById(R.id.videoTitle);     
        ratedSpinner = (Spinner)this.findViewById(R.id.ratedSpinner);
        ratedSpinner.setSelection(5);
        progressCircle = (ProgressBar)this.findViewById(R.id.progressScan);
        progressCircle.setVisibility(View.INVISIBLE);
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
    		addTask.execute(ipAddress, upcText.getText().toString(), videoTitle.getText().toString(), videoDirector.getText().toString(),
    				ratedSpinner.getSelectedItem().toString(), videoRuntime.getText().toString(), videoYear.getText().toString());

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
    					progressCircle.setVisibility(View.VISIBLE);
    					GetVideoTask getTask = new GetVideoTask(this);
    					getTask.execute(ipAddress, upc);
    			       
    				}
    			}
    		}
    	}
    }
    
    private void transformSoapResponse(SoapObject response) {

		String title = response.getPropertySafelyAsString("title","");
		if(!title.isEmpty()) {
			videoTitle.setText(title);
			
			// if Director doesn't exist... leave it empty
			SoapObject director = (SoapObject) response.getPropertySafely("director",new SoapObject("", ""));
			if(!director.toString().isEmpty()) {
				String first = director.getPropertySafelyAsString("firstName","");
				String last = director.getPropertySafelyAsString("lastName","");
				if(!(last.isEmpty() && first.isEmpty())) {
				    videoDirector.setText(first + " " + last);
				}
					
			}
			else {
				videoDirector.setText("");
			}
		
			String rated = response.getPropertySafelyAsString("rated","UNRATED");
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
			if(rated.equals("UNRATED")) 
				ratedSpinner.setSelection(5);
			
			videoYear.setText(response.getPropertySafelyAsString("year",""));
			videoRuntime.setText(response.getPropertySafelyAsString("runtime","")); 
		} else {
			AlertDialog ad = new AlertDialog.Builder(this).create();  
			ad.setCancelable(false); // This blocks the 'BACK' button  
			ad.setButton("OK", new DialogInterface.OnClickListener() {  
			    public void onClick(DialogInterface dialog, int which) {  
			        dialog.dismiss();                      
			    }  
			});
			ad.setMessage("No results found");  
			ad.show();

		}
		
    }

	public void onEvent(TaskEvent task) {
		
		TaskEvent.Status taskStatus = task.getStatus();
    	String action = task.getTask();
    	
		
    	if("GET_VIDEO".equals(action)){
    		progressCircle.setVisibility(View.INVISIBLE);
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			Log.i("Interfaces", "GET_VIDEO Success");
    			transformSoapResponse((SoapObject)task.getResult());
    		} else {
    			AlertDialog ad = new AlertDialog.Builder(this).create();  
    			ad.setCancelable(false); // This blocks the 'BACK' button  
    			ad.setButton("OK", new DialogInterface.OnClickListener() {  
    			    public void onClick(DialogInterface dialog, int which) {  
    			        dialog.dismiss();                      
    			    }  
    			});
    			ad.setMessage("No response from video lookup services");  
    			ad.show();
    		}
    	}
    	else if("ADD_VIDEO".equals(action)){
    		if(taskStatus == TaskEvent.Status.SUCCESS){
    			String response = ((SoapObject)task.getResult()).getPropertyAsString(0);
    			if(!"success".equals(response)){
    					AlertDialog ad = new AlertDialog.Builder(this).create();  
    					ad.setCancelable(false); // This blocks the 'BACK' button  
    					ad.setButton("OK", new DialogInterface.OnClickListener() {  
    						public void onClick(DialogInterface dialog, int which) {  
    							dialog.dismiss();                      
    						}  
    					});
    					ad.setMessage(response);
    					ad.show();
    			} else {
    				// Show toast notification
    				appState.getVideoList().add(getVideoFromFields());
    				Toast.makeText(this.getApplicationContext(), 
    	        		videoTitle.getText().toString() + " succesfully added to collection!", Toast.LENGTH_SHORT).show();
    				clearAllFields();
    			}
    		}
    		else
    		{
    			AlertDialog ad = new AlertDialog.Builder(this).create();  
    			ad.setCancelable(false); // This blocks the 'BACK' button  
    			ad.setButton("OK", new DialogInterface.OnClickListener() {  
    			    public void onClick(DialogInterface dialog, int which) {  
    			        dialog.dismiss();                      
    			    }  
    			});
    			ad.setMessage("Error sending video to server");  
    			ad.show();
    		}
    	}
    	
	}
	
	private void clearAllFields() {
        upcText.setText("");
        videoDirector.setText("");
        videoYear.setText(""); 
        videoRuntime.setText("");
        videoTitle.setText("");     
        ratedSpinner.setSelection(0); 
	}
	
	private VideoMobile getVideoFromFields() {
		VideoMobile video = new VideoMobile();
		AlertDialog ad = new AlertDialog.Builder(this).create();  
		ad.setCancelable(false); // This blocks the 'BACK' button  
		ad.setButton("OK", new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int which) {  
		        dialog.dismiss();                      
		    }  
		});
		
		ad.setMessage("Error sending video to server");  

		if(!videoTitle.getText().toString().isEmpty()) 
			video.setTitle(videoTitle.getText().toString());
		else {
			ad.setMessage("Title must be Entered");
			ad.show();
			return null;
		}
			
		video.setDirector(videoDirector.getText().toString());
		video.setRated(ratedSpinner.getSelectedItem().toString());
		if(videoYear.getText().toString().length() > 0)
		video.setYear(Integer.parseInt(videoYear.getText().toString()));
		if(videoRuntime.getText().toString().length() > 0)
		video.setRuntime(Integer.parseInt(videoRuntime.getText().toString()));

		return video;
	}
}

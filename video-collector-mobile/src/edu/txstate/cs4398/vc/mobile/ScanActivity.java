package edu.txstate.cs4398.vc.mobile;

import java.util.UUID;

import edu.txstate.cs4398.vc.mobile.utils.IntentIntegrator;
import edu.txstate.cs4398.vc.mobile.utils.IntentResult;
import android.os.Bundle;
import android.annotation.SuppressLint;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class ScanActivity extends Activity implements View.OnClickListener {
	Button scanButton;
	Button addVideoButton;
	EditText upcText;
	EditText videoTitle;
	EditText videoDirector;
	EditText videoYear;
	EditText videoRuntime;
	Spinner ratedSpinner;
	private static final String NAMESPACE = "http://services.desktop.vc.cs4398.txstate.edu/";
	private static String URL;
	private static final String GET_VIDEO_METHOD = "getVideoByUPC";
	private static final String ADD_VIDEO_METHOD = "addVideo";
	private static final String GET_SOAP_ACTION =  "http://services.desktop.vc.cs4398.txstate.edu/getVideoByUPC";
	private static final String ADD_SOAP_ACTION =  "http://services.desktop.vc.cs4398.txstate.edu/addVideo";


	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        VideoApp appState = ((VideoApp)getApplicationContext());
        String ipAddress = appState.getWebServiceAddress();
		URL = "http://"+ipAddress+":8796/MobileServices?WSDL";
		
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
    		addVideo();
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
    					
    			        SoapObject request = new SoapObject(NAMESPACE, GET_VIDEO_METHOD);

    			        request.addPropertyIfValue("arg0", upc);
    			        
    			        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			        envelope.setOutputSoapObject(request);

    			        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    			        try {
    					androidHttpTransport.call(GET_SOAP_ACTION, envelope);
    			        

    					SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
    					SoapObject videoResult = (SoapObject) resultRequestSOAP.getProperty(0);
    					transformSoapResponse(videoResult);

    			        } catch (Exception e) {
    			        	Log.d("SOAPERROR", e.getMessage());
    			        	
    			        }
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
    
    private void addVideo()  {
    	
    	AlertDialog ad = new AlertDialog.Builder(this).create();  
		ad.setCancelable(false); // This blocks the 'BACK' button  
		ad.setMessage("Error sending video to server");  
		ad.setButton("OK", new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int which) {  
		        dialog.dismiss();                      
		    }  
		});
		//ad.show();
		
		/*MobileServices services;
		 * this doesn't work...
    	MobileServicesImplService service = new MobileServicesImplService();*/
    	
    	//services = service.getMobileServicesImplPort();
		        
        SoapObject request = new SoapObject(NAMESPACE, ADD_VIDEO_METHOD);
        
        request.addPropertyIfValue("arg0", upcText.getText().toString());
        request.addPropertyIfValue("arg1", videoTitle.getText().toString());
       
       // request.addProperty("arg0",video);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
       
        try {
        	androidHttpTransport.call(ADD_SOAP_ACTION, envelope);
        	//SoapObject addResult = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
			ad.show();  
			return;
		}
    }}

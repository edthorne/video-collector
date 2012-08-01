package edu.txstate.cs4398.vc.mobile;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<String, Void, SoapObject> {

	private static final String NAMESPACE = "http://services.desktop.vc.cs4398.txstate.edu/";
	private static String URL;
	private static final String ECHO_METHOD = "echo";
	private static final String ECHO_SOAP_ACTION =  "http://services.desktop.vc.cs4398.txstate.edu/echo";
	private EventHandler event;

	protected ConnectTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
	@Override
	protected SoapObject doInBackground(String... address) {
		URL = "http://"+address[0]+":8796/MobileServices?WSDL";
		SoapObject request = new SoapObject(NAMESPACE, ECHO_METHOD);
        
		request.addPropertyIfValue("arg0", MobileClient.CONNECT_MESSAGE);
       
       // request.addProperty("arg0",video);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        SoapObject addResult = null;
        try {
        	androidHttpTransport.call(ECHO_SOAP_ACTION, envelope);
        	addResult = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addResult;
	}
	
	@Override
	protected void onPostExecute(SoapObject result){
		Task resultTask = new Task("CONNECT");
		resultTask.setTaskResult(result.getProperty(0).toString());	// Result is first (and only property) the SoapObject
		event.notifyEvent(resultTask);
	}

}

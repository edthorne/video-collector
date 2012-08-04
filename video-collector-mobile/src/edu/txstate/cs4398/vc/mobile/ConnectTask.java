package edu.txstate.cs4398.vc.mobile;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import edu.txstate.cs4398.vc.mobile.TaskEvent.Status;

import android.os.AsyncTask;

public class ConnectTask extends BaseTask<String, Void, SoapObject> {

	private static String URL;
	private final String ECHO_METHOD = "echo";
	private final String ECHO_SOAP_ACTION =  "\"http://services.desktop.vc.cs4398.txstate.edu/echo\"";
	private final String CONNECT_MESSAGE = "TESTING CONNECTION";
	

	protected ConnectTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
	@Override
	protected SoapObject doInBackground(String... address) {
		URL = "http://"+address[0]+":8796/MobileServices?WSDL";
		SoapObject request = new SoapObject(NAMESPACE, ECHO_METHOD);
        
		request.addPropertyIfValue("arg0", CONNECT_MESSAGE);
        
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
		TaskEvent<String> task = new TaskEvent<String>("CONNECT");
		
		if(CONNECT_MESSAGE.equals(result.getProperty(0).toString()))
			task.setStatus(TaskEvent.Status.SUCCESS);
		else
			task.setStatus(TaskEvent.Status.FAIL);
		
		event.notifyEvent(task);
	}

}

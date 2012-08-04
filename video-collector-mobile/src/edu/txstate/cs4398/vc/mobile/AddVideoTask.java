package edu.txstate.cs4398.vc.mobile;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class AddVideoTask extends BaseTask<String, Void, SoapObject> {

	private static final String ADD_VIDEO_METHOD = "addVideo";
	private static final String ADD_SOAP_ACTION =  "\"http://services.desktop.vc.cs4398.txstate.edu/addVideo\"";
	private static String URL;
	
	public AddVideoTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
	
	@Override
	protected SoapObject doInBackground(String... data) {
		URL = "http://"+data[0]+":8796/MobileServices?WSDL";
		
		        
        SoapObject request = new SoapObject(NAMESPACE, ADD_VIDEO_METHOD);
        
        request.addPropertyIfValue("upc", data[1]);
        request.addPropertyIfValue("title", data[2]);
        request.addPropertyIfValue("director",data[3]);
        request.addPropertyIfValue("rated", data[4]);
        request.addPropertyIfValue("runtime", data[5]);
        request.addPropertyIfValue("year", data[6]);
       
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
       
        SoapObject addResult = null;
        try {
        	androidHttpTransport.call(ADD_SOAP_ACTION, envelope);
        	addResult = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
     
		return addResult;
	}
	
	@Override
	protected void onPostExecute(SoapObject soap){		// If we reach this method then it's guaranteed we have a successful result
		TaskEvent<SoapObject> task = new TaskEvent<SoapObject>("ADD_VIDEO");
		if(soap != null)
			task.setStatus(TaskEvent.Status.SUCCESS);
		else
			task.setStatus(TaskEvent.Status.FAIL);
		task.setResult(soap);
		event.notifyEvent(task);
	}

}

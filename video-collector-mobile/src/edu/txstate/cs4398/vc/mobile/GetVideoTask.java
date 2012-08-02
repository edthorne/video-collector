package edu.txstate.cs4398.vc.mobile;

import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class GetVideoTask extends BaseTask<String, Void, SoapObject>  {

	private static String URL;
	private static final String GET_VIDEO_METHOD = "getVideoByUPC";
	private static final String GET_SOAP_ACTION =  "http://services.desktop.vc.cs4398.txstate.edu/getVideoByUPC";
	
	public GetVideoTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
	
	@Override
	protected SoapObject doInBackground(String... data) {
		URL = "http://"+data[0]+":8796/MobileServices?WSDL";
		
		SoapObject request = new SoapObject(NAMESPACE, GET_VIDEO_METHOD);

        request.addPropertyIfValue("arg0", data[1]);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        final HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
			androidHttpTransport.call(GET_SOAP_ACTION, envelope);
            TimerTask task = new TimerTask( ) { public void run( ) { Log.d("DEBUG",("no service.."));androidHttpTransport.reset(); } };

            new Timer().schedule( task, 5000 );
				androidHttpTransport.call(GET_SOAP_ACTION, envelope);

			task.cancel( );           // cancel the timeout

			SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
			return (SoapObject) resultRequestSOAP.getProperty(0);		
			
        } catch (Exception e) {
        	Log.d("SOAPERROR", e.getMessage());
        	return null;
        }
	}
	
	@Override
	protected void onPostExecute(SoapObject soap){		// If we reach this method then it's guaranteed we have a successful result
		TaskEvent<SoapObject> task = new TaskEvent<SoapObject>("GET_VIDEO");
		if(soap != null)
			task.setStatus(TaskEvent.Status.SUCCESS);
		else
			task.setStatus(TaskEvent.Status.FAIL);
		task.setResult(soap);
		event.notifyEvent(task);
	}

}

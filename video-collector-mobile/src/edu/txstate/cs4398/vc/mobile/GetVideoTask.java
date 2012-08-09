package edu.txstate.cs4398.vc.mobile;

import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import edu.txstate.cs4398.vc.mobile.video.VideoMobile;

import android.util.Log;

/**
 * Task to retrieve a video from the video lookup services
 * @author mnosler
 *
 */
public class GetVideoTask extends BaseTask<String, Void, VideoMobile>  {

	private static String URL;
	private static final String GET_VIDEO_UPC = "lookupVideoByUPC";
	private static final String GET_VIDEO_TITLE = "lookupVideoByTitle";
	private static final String GET_SOAP_ACTION_UPC =  "\"http://services.desktop.vc.cs4398.txstate.edu/lookupVideoByUPC\"";
	private static final String GET_SOAP_ACTION_TITLE =  "\"http://services.desktop.vc.cs4398.txstate.edu/lookupVideoByTitle\"";
	
	public GetVideoTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
	
	@Override
	protected VideoMobile doInBackground(String... data) {
		URL = "http://"+data[0]+":8796/MobileServices?WSDL";
		
		String name = null;
		String action = null;
		
		if(data[1].equals("UPC")) {
			name = GET_VIDEO_UPC;
			action = GET_SOAP_ACTION_UPC;
		} else if(data[1].equals("TITLE")) {
			name = GET_VIDEO_TITLE;
			action = GET_SOAP_ACTION_TITLE;
		}
		SoapObject request = new SoapObject(NAMESPACE, name);

        request.addPropertyIfValue("arg0", data[2]);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        final HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
			androidHttpTransport.call(action, envelope);
            TimerTask task = new TimerTask( ) { public void run( ) { Log.d("DEBUG",("no service.."));androidHttpTransport.reset(); } };

            new Timer().schedule( task, 5000 );
				androidHttpTransport.call(action, envelope);

			task.cancel( );           // cancel the timeout

			SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
			return VideoMobile.getVideoFromSoap((SoapObject)resultRequestSOAP.getProperty(0));		
			
        } catch (Exception e) {
        	Log.d("SOAPERROR", e.getMessage());
        	return null;
        }
	}
	
	@Override
	protected void onPostExecute(VideoMobile video){		// If we reach this method then it's guaranteed we have a successful result
		TaskEvent<VideoMobile> task = new TaskEvent<VideoMobile>("GET_VIDEO");
		if(video != null)
			task.setStatus(TaskEvent.Status.SUCCESS);
		else
			task.setStatus(TaskEvent.Status.FAIL);
		task.setResult(video);
		event.notifyEvent(task);
	}

}

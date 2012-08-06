package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import edu.txstate.cs4398.vc.mobile.video.VideoMobile;

import android.util.Log;

/**
 * This task will get the current collection from the desktop application
 * @author mnosler
 *
 */
public class GetCollectionTask extends BaseTask<String, Void, List<VideoMobile>> {

	private static String URL;
	private static final String GET_COLLECTION = "getCollection";
	private static final String GET_SOAP_ACTION =  "\"http://services.desktop.vc.cs4398.txstate.edu/getCollection\"";
	
	public GetCollectionTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
	
	@Override
	protected List<VideoMobile> doInBackground(String... data) {
		URL = "http://"+data[0]+":8796/MobileServices?WSDL";
		Log.d("DEBUG", "in collection execute..");
		SoapObject request = new SoapObject(NAMESPACE, GET_COLLECTION);

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
			
			return transformList( (SoapObject) resultRequestSOAP.getProperty(0) );	

        } catch (Exception e) {
        	Log.d("SOAPERROR", e.getMessage());
        	return null;
        }
	}
	
	@Override
	protected void onPostExecute(List<VideoMobile> list){		// If we reach this method then it's guaranteed we have a successful result
		TaskEvent<List<VideoMobile>> task = new TaskEvent<List<VideoMobile>>("GET_COLLECTION");
		if(list != null && list.size() > 0)
			task.setStatus(TaskEvent.Status.SUCCESS);
		else
			task.setStatus(TaskEvent.Status.FAIL);
		task.setResult(list);
		event.notifyEvent(task);
	}
	
	private List<VideoMobile> transformList(SoapObject soap) {
		List<VideoMobile> list = new ArrayList<VideoMobile>();
		Log.d("DEBUG", "in transform list..");

		soap = (SoapObject) soap.getProperty("videos");
		int size = soap.getPropertyCount();
		
		for(int i = 0; i < size; i++) {
			list.add(getVideoFromSoap( (SoapObject) soap.getProperty(i)));	
		}
		return list;
	}
	
	private VideoMobile getVideoFromSoap (SoapObject response){
		VideoMobile video = new VideoMobile();
		String title = response.getPropertySafelyAsString("title");
		video.setTitle(title);
		
		SoapObject director = (SoapObject) response.getPropertySafely("director", new SoapObject("",""));
		if(!director.toString().isEmpty()) {
			String first = director.getPropertySafelyAsString("firstName","");
			String last = director.getPropertySafelyAsString("lastName","");
			if(!(last.isEmpty() && first.isEmpty())) {
				video.setDirector(first + " " + last);
			}
				
		}
		
		String rated = response.getPropertySafelyAsString("rated","UNRATED");
		video.setRated(rated);
		int year = Integer.parseInt(response.getPropertySafelyAsString("year"));
		video.setYear(year);
		int runtime = Integer.parseInt(response.getPropertySafelyAsString("runtime"));
		video.setRuntime(runtime);
		Log.d("adding video", video.getTitle());
		video.setImageURL(response.getPropertySafelyAsString("imageURL",""));
		video.setImageByURL();
		return video;
	}

}

package edu.txstate.cs4398.vc.mobile.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import edu.txstate.cs4398.vc.mobile.VideoApp;
import edu.txstate.cs4398.vc.model.mobile.VideoMobile;
import edu.txstate.cs4398.vc.model.mobile.VideoMobileList;

import android.content.Context;
import android.util.Log;

/**
 * This class is used for reading and writing the video list to xml for persistence
 * @author mnosler
 *
 */
public class CollectionReadWriter {
	

	public CollectionReadWriter() {
		
	}
	
	/** 
	 * gets a list of videos from the application data xml
	 * @param ctx
	 * @return all the video data stored on the device
	 */
	public List<VideoMobile> getVideosFromXml(Context ctx)
	{
		try {

			Serializer serializer = new Persister();

			FileInputStream fis = ctx.openFileInput(VideoApp.VIDEO_FILENAME);
			VideoMobileList vmlist = serializer.read(VideoMobileList.class, fis);

			fis.close();
			return vmlist.getVideos();
		
		} catch(Exception e) {
			Log.i("ERROR READING XML", e.getMessage());
			return null;
		}
		
	}
	
	/**
	 * writes the current Videos from the application to xml on the android device
	 * saved as private application data
	 * @param ctx
	 */
	public void writeVideosToXml(Context ctx)
	{
		try {
			VideoApp app = (VideoApp) ctx.getApplicationContext();
			List<VideoMobile> vm = app.getVideoList();
			VideoMobileList vmlist = new VideoMobileList();
			
			Serializer serializer = new Persister();
			
			vmlist.setVideos(vm);
			
			FileOutputStream fos = ctx.openFileOutput(VideoApp.VIDEO_FILENAME, Context.MODE_PRIVATE);
			serializer.write(vmlist, fos);
			
			fos.close();
		} catch(Exception e) {
			Log.i("ERROR WRITING XML", e.getMessage());
		}
	}
	
}

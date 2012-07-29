package edu.txstate.cs4398.vc.mobile;

import edu.txstate.cs4398.vc.model.Collection;
import android.app.Application;

public class VideoApp extends Application {

	private String webServiceAddress;
	private Collection videoCollection;
	
	public String getWebServiceAddress() {
		return webServiceAddress;
	}

	public void setWebServiceAddress(String webServiceAddress) {
		this.webServiceAddress = webServiceAddress;
	}

	public Collection getVideoCollection() {
		return videoCollection;
	}

	public void setVideoCollection(Collection videoCollection) {
		this.videoCollection = videoCollection;
	}
	
}

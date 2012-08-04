package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.List;

import edu.txstate.cs4398.vc.model.Video;
import android.app.Application;

public class VideoApp extends Application {

	private String webServiceAddress;
	private List<Video> videoList = new ArrayList<Video>();
	
	public String getWebServiceAddress() {
		return webServiceAddress;
	}

	public void setWebServiceAddress(String webServiceAddress) {
		this.webServiceAddress = webServiceAddress;
	}

	public List<Video> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}
	
}

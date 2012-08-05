package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.List;

import edu.txstate.cs4398.vc.model.mobile.VideoMobile;
import android.app.Application;

public class VideoApp extends Application {

	private String webServiceAddress;
	private List<VideoMobile> videoList = new ArrayList<VideoMobile>();
	public static final String VIDEO_FILENAME = "video_collection";

	public String getWebServiceAddress() {
		return webServiceAddress;
	}

	public void setWebServiceAddress(String webServiceAddress) {
		this.webServiceAddress = webServiceAddress;
	}

	public List<VideoMobile> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<VideoMobile> videoList) {
		this.videoList = videoList;
	}
	
}

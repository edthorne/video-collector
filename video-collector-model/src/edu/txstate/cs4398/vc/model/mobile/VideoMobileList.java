package edu.txstate.cs4398.vc.model.mobile;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This is a list of videos for easy marshalling without jaxb
 * @author mnosler
 *
 */
@Root
public class VideoMobileList {


	@ElementList(name = "videos", entry = "video")
	private List<VideoMobile> videos = new ArrayList<VideoMobile>();

	public List<VideoMobile> getVideos() {
		return videos;
	}

	public void setVideos(List<VideoMobile> videos) {
		this.videos = videos;
	}


}

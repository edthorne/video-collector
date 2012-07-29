package edu.txstate.cs4398.vc.desktop.services;

import javax.jws.WebService;

import edu.txstate.cs4398.vc.model.Collection;
import edu.txstate.cs4398.vc.model.Video;

@WebService(endpointInterface="edu.txstate.cs4398.vc.desktop.services.MobileServices")
public class MobileServicesImpl implements MobileServices {

	@Override
	public String echo(String data) {
		return data;
	}

	@Override
	public Video getVideoByUPC(String upc)
	{
		Video video = new Video();
		
		try{
			
		VideoLookupService service = new VideoLookupService();
		String product = service.getProductName(upc);
		video.setUpc(upc);
		video = service.getVideoByName(product, video);
		
		//We want to use the full name from the UPC search
		video.setTitle(product);
		} catch(Exception e){
			return video;
		}
		return video;
	}
	
	@Override
	public String getProductName(String upc)
	{
		try{
		VideoLookupService service = new VideoLookupService();

		return service.getProductName(upc);
		} catch(Exception e) {
			return "";
		}
	}
	
	@Override
	public Video getVideoByName(String name)
	{
		Video video = new Video();
		
		try{
		VideoLookupService service = new VideoLookupService();

		return service.getVideoByName(name,video);
		} catch(Exception e) {
			return video;
		}
	}


	@Override
	public boolean addVideo(String upc, String title) {
		Collection collection = new Collection();
		Video video = new Video(title);
		video.setUpc(upc);
		collection.addVideo(video);
		System.out.println("Added video:");
		System.out.println(video.getUpc());
		System.out.println(video.getTitle());
		return true;
	}

}

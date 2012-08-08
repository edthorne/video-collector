package edu.txstate.cs4398.vc.desktop.services;

import java.util.Set;

import javax.jws.WebService;

import edu.txstate.cs4398.vc.desktop.model.CollectorModel;
import edu.txstate.cs4398.vc.model.Collection;
import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Rating;
import edu.txstate.cs4398.vc.model.Video;

@WebService(endpointInterface = "edu.txstate.cs4398.vc.desktop.services.MobileServices", serviceName = "MobileServices")
public class MobileServicesImpl implements MobileServices {
	private CollectorModel model;
	private VideoLookupService service;

	public MobileServicesImpl() {

	}

	public MobileServicesImpl(CollectorModel model) {
		this.model = model;
	}

	public void setCollectorModel(CollectorModel model) {
		this.model = model;
	}
	
	public void setVideoLookupService(VideoLookupService service) {
		this.service = service;
	}

	@Override
	public String echo(String data) {
		return data;
	}

	@Override
	public Video lookupVideoByUPC(String upc) {
		Video video = new Video();

		try {

			String product = service.getProductName(upc);
			video.setUpc(upc);
			video = service.getVideoByName(product, video);

			// We want to use the full name from the UPC search
			video.setTitle(product);
		} catch (Exception e) {
			return video;
		}
		return video;
	}

	@Override
	public Video lookupVideoByTitle(String title) {
		Video video = new Video();

		try {
			return service.getVideoByName(title, video);
		} catch (Exception e) {
			return video;
		}
	}

	@Override
	public String addVideo(String upc, String title, String director,
			Rating rated, int runtime, int year, String imgUrl) {
		try {
			Video video = new Video(title);
			video.setUpc(upc);

			if (title.isEmpty())
				throw new IllegalArgumentException("Title can not be blank");

			if (!director.isEmpty())
				video.setDirector(Person.fromString(director));

			video.setRated(rated);
			video.setRuntime(runtime);
			video.setYear(year);
			video.setImageURL(imgUrl);

			model.getCollection().addVideo(video);
			System.out.println("Added video:");
			System.out.println(video.getUpc());
			System.out.println(video.getTitle());
			System.out.println(video.getDirector());
			System.out.println(video.getRated());
			System.out.println(video.getRuntime());
			System.out.println(video.getYear());

			return "success";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@Override
	public Collection getCollection() {
		return model.getCollection();
	}

	@Override
	public Set<String> getCategories() {
		return model.getCollection().getCategories();
	}

	@Override
	public Set<Person> getPeople() {
		return model.getCollection().getPeople();
	}

}

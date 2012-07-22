package edu.txstate.cs4398.vc.desktop.services;

import javax.jws.WebService;

@WebService(endpointInterface="edu.txstate.cs4398.vc.desktop.services.MobileServices")
public class MobileSerivcesImpl implements MobileServices {

	@Override
	public String echo(String data) {
		return data;
	}

}

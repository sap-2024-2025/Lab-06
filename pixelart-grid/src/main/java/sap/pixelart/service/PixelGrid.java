package sap.pixelart.service;

import java.net.URL;

import sap.pixelart.service.application.PixelGridImpl;
import sap.pixelart.service.application.PixelGridRegistryAsyncAPI;
import sap.pixelart.service.infrastructure.*;

public class PixelGrid {

	private PixelGridImpl service;
	private RESTPixelGridController adapter;
	private URL localAddress;
	
	public PixelGrid(String pixelGridId, URL localAddress) {
    	service = new PixelGridImpl(pixelGridId);
    	this.localAddress = localAddress;
 	}
	
	public void launch() {
		adapter = new RESTPixelGridController(localAddress.getPort());	    	
    	adapter.init(service);
	}
}

package sap.pixelart.service;

import java.net.URI;

import sap.pixelart.service.application.PixelGridRegistryAsyncAPI;
import sap.pixelart.service.infrastructure.PixelGridRegistryProxy;

public class PixelGridLauncher {
		
	public static final String SERVICE_ADDRESS = "http://localhost:9100"; 
	public static final String REGISTRY_ADDRESS = "http://localhost:9000"; 
	
    public static void main(String[] args) throws Exception {

    	var gridId = "my-grid-01";
    	
    	var serviceAddress = new URI(SERVICE_ADDRESS).toURL();    	
    	PixelGrid service = new PixelGrid(gridId, serviceAddress);
    	service.launch();
    	
    	var registryAddress = new URI(REGISTRY_ADDRESS).toURL();    	
    	PixelGridRegistryAsyncAPI registry = new PixelGridRegistryProxy(registryAddress);
    	registry.registerPixelGrid(gridId, SERVICE_ADDRESS);
    }
}

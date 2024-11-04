package sap.pixelart.registry.infrastructure;

import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.Vertx;
import sap.pixelart.registry.domain.PixelGridRegistryAPI;

/**
 * 
 * Adapter implementing a REST API  
 * 
 * - interacting with the application layer through the PixelArtAPI interface 
 * 
 * @author aricci
 *
 */
public class RESTPixelGridRegistryController {
    static Logger logger = Logger.getLogger("[RestPixelArtRegistryController]");	
	private int port;
	private RESTPixelGridRegistryControllerVerticle service;
	
	public RESTPixelGridRegistryController(int port) {	
		this.port = port;
	}
		
	public void init(PixelGridRegistryAPI pixelArtAPI) {
    	Vertx vertx = Vertx.vertx();
		this.service = new RESTPixelGridRegistryControllerVerticle(port, pixelArtAPI);
		vertx.deployVerticle(service);	
	}


}

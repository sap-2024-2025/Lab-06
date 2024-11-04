package sap.pixelart.library;

import io.vertx.core.Future;

public interface PixelGridRegistryAsyncAPI  {
	
	Future<String> lookupPixelGrid(String name) throws Exception;
		
}

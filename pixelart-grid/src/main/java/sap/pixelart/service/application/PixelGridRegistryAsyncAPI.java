package sap.pixelart.service.application;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface PixelGridRegistryAsyncAPI  {
	
	Future<JsonObject> registerPixelGrid(String serviceName, String serviceAddress) throws Exception;
		
}

package sap.pixelart.service.infrastructure;

import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.*;
import io.vertx.core.json.*;
import sap.pixelart.service.application.PixelGridRegistryAsyncAPI;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

public class PixelGridRegistryProxy implements PixelGridRegistryAsyncAPI {

	public static String REGISTER_GRID_ENDPOINT = "/api/pixel-grids";
	public static String GRID_ID = "gridId";
	public static String GRID_ADDRESS = "gridAddress";

    static Logger logger = Logger.getLogger("[Pixel Grid Registry Proxy]");	
	private Vertx vertx;
	private URL registryAddress;
	
	public PixelGridRegistryProxy(URL registryAddress) {
		vertx = Vertx.vertx();
		this.registryAddress = registryAddress;
	}
	
	@Override
	public Future<JsonObject> registerPixelGrid(String serviceName, String serviceAddress) throws Exception {
		// TODO Auto-generated method stub
		logger.log(Level.INFO, "Register Pixel Grid " + serviceName + "...");

		HttpClientOptions options = new HttpClientOptions()
				.setDefaultHost(registryAddress.getHost())
				.setDefaultPort(registryAddress.getPort());
		HttpClient client = vertx.createHttpClient(options);
	
		Promise<JsonObject> p = Promise.promise();
		client
		.request(HttpMethod.POST, REGISTER_GRID_ENDPOINT)		
		.onSuccess(req -> {
			req.response().onSuccess(response -> {
				response.body().onSuccess(buf -> {
					logger.log(Level.INFO, "Pixel Grid " + serviceName + " registered.");
					JsonObject obj = buf.toJsonObject();
					p.complete(obj);
				});
			});
			JsonObject body = new JsonObject();
			body.put(GRID_ID, serviceName);
			body.put(GRID_ADDRESS, serviceAddress);
			req.send(body.encodePrettily());
		})
		.onFailure(f -> {
			logger.log(Level.INFO, "Pixel Grid " + serviceName + " not registered.");
			p.fail(f.getMessage());
		});
		
		return p.future();
		
	}

}

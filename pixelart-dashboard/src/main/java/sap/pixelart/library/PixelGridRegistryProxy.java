package sap.pixelart.library;

import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.*;
import io.vertx.core.json.*;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

public class PixelGridRegistryProxy implements PixelGridRegistryAsyncAPI {

	public static String REGISTRY_API_BASE = "/api/pixel-grids";
	public static String GRID_ADDRESS = "gridAddress";

    static Logger logger = Logger.getLogger("[Pixel Grid Registry Proxy]");	
	private Vertx vertx;
	private URL registryAddress;
	
	public PixelGridRegistryProxy(URL registryAddress) {
		vertx = Vertx.vertx();
		this.registryAddress = registryAddress;
	}
	
	
	@Override
	public Future<String> lookupPixelGrid(String gridId) throws Exception {
		// TODO Auto-generated method stub
		logger.log(Level.INFO,"Lookup address of pixel Grid " + gridId + " at registry: " + registryAddress);

		HttpClientOptions options = new HttpClientOptions()
				.setDefaultHost(registryAddress.getHost())
				.setDefaultPort(registryAddress.getPort());
		HttpClient client = vertx.createHttpClient(options);
	
		String requestGridInfoEndpoint = REGISTRY_API_BASE + "/" + gridId;
		Promise<String> p = Promise.promise();
	
		client
		.request(HttpMethod.GET, requestGridInfoEndpoint)		
		.onSuccess(req -> {
			req.response().onSuccess(response -> {
				// System.out.println("Received response with status code " + response.statusCode());
				response.body().onSuccess(buf -> {
					JsonObject obj = buf.toJsonObject();
					String gridAddress = obj.getString(GRID_ADDRESS);
					p.complete(gridAddress);
				});
			});
			req.send();
		})
		.onFailure(f -> {
			p.fail(f.getMessage());
		});
		
		return p.future();
		
	}

}

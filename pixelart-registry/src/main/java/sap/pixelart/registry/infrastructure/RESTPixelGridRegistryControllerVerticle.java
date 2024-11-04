package sap.pixelart.registry.infrastructure;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.*;
import io.vertx.ext.web.*;
import sap.pixelart.registry.domain.*;

/**
 * 
 * Verticle impementing the behaviour of a REST Adapter for the 
 * PixelArt microservice
 * 
 * @author aricci
 *
 */
public class RESTPixelGridRegistryControllerVerticle extends AbstractVerticle {

	private int port;
	private PixelGridRegistryAPI pixelGridRegistryAPI;
	static Logger logger = Logger.getLogger("[RegistryControllerVerticle]");
	
	public static String REGISTER_GRID_ENDPOINT = "/api/pixel-grids";
	public static String GET_GRID_INFO_ENDPOINT = "/api/pixel-grids/:gridId";
	public static String GET_GRIDS_ENDPOINT = "/api/pixel-grids";
	public static String GRID_ID = "gridId";
	public static String GRID_ADDRESS = "gridAddress";
	public static String GRIDS_ID = "gridIds";

	public RESTPixelGridRegistryControllerVerticle(int port, PixelGridRegistryAPI appAPI) {
		this.port = port;
		this.pixelGridRegistryAPI = appAPI;
		logger.setLevel(Level.INFO);
	}

	public void start() {
		logger.log(Level.INFO, "PixelArt Registry initializing...");
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);

		/* configure the HTTP routes following a REST style */
		
		router.route(HttpMethod.POST, REGISTER_GRID_ENDPOINT).handler(this::registerPixelGrid);
		router.route(HttpMethod.GET, GET_GRIDS_ENDPOINT).handler(this::getPixelGridsId);
		router.route(HttpMethod.GET, GET_GRID_INFO_ENDPOINT).handler(this::getPixelGridInfo);

		/* start the server */
		
		server
		.requestHandler(router)
		.listen(port);

		logger.log(Level.INFO, "PixelArt Registry ready - port: " + port);
	}

	/* List of handlers, mapping the API */
	
	protected void registerPixelGrid(RoutingContext context) {
		logger.log(Level.INFO, "Register new pixel grid request - " + context.currentRoute().getPath());
		JsonObject reply = new JsonObject();
		context.request().handler(buf -> {
			JsonObject gridInfo = buf.toJsonObject();
			var name = gridInfo.getString(GRID_ID);
			var addr = gridInfo.getString(GRID_ADDRESS);			
			try {
				var url = new URI(addr).toURL();
				pixelGridRegistryAPI.registerPixelGrid(name, url);
				reply.put(GRID_ID, name);
				reply.put(GRID_ADDRESS, addr);
				sendReply(context.response(), reply);
			} catch (Exception ex) {
				ex.printStackTrace();
				sendServiceError(context.response());
			}
		});
	}

	protected void getPixelGridsId(RoutingContext context) {
		logger.log(Level.INFO, "Get pixel grids Id - " + context.currentRoute().getPath());
		var l = pixelGridRegistryAPI.getPixelGridsId();
		JsonObject reply = new JsonObject();
		JsonArray ar = new JsonArray();
		for (var id: l) {
			ar.add(id);
		}
		reply.put(GRIDS_ID, ar);
		sendReply(context.response(), reply);
	}

	protected void getPixelGridInfo(RoutingContext context) {
		logger.log(Level.INFO, "Get pixel grid info request - " + context.currentRoute().getPath());
		String gridId = context.pathParam(GRID_ID);
		var url = pixelGridRegistryAPI.lookupPixelGrid(gridId);
		JsonObject reply = new JsonObject();
		if (url.isPresent()) {
			reply.put(GRID_ID, gridId);
			reply.put(GRID_ADDRESS, url.get().toString());
			sendReply(context.response(), reply);
		} else {
			sendServiceError(context.response());
		}
	}

	/* Aux methods */
	
	protected void log(String msg) {
		logger.log(Level.INFO, msg);
	}

	private void sendReply(HttpServerResponse response, JsonObject reply) {
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}
	
	private void sendBadRequest(HttpServerResponse response, JsonObject reply) {
		response.setStatusCode(400);
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}

	private void sendServiceError(HttpServerResponse response) {
		response.setStatusCode(500);
		response.putHeader("content-type", "application/json");
		response.end();
	}

}

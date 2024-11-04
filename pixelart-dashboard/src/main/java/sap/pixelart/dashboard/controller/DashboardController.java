package sap.pixelart.dashboard.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.*;
import sap.pixelart.dashboard.model.DashboardModel;
import sap.pixelart.dashboard.view.DashboardView;
import sap.pixelart.dashboard.view.DashboardViewListener;
import sap.pixelart.library.PixelArtServiceLib;
import sap.pixelart.library.PixelGridAsyncAPI;
import sap.pixelart.library.PixelGridEventObserver;

public class DashboardController extends AbstractVerticle implements DashboardViewListener, PixelGridEventObserver {
    static Logger logger = Logger.getLogger("[AsyncController]");	

	private String registryAddress;
	private String gridId;
	private DashboardModel pixelLocalViewModel;
	private DashboardView view;
	private String brushId;
	private PixelGridAsyncAPI proxy;
	
	public static final String SELECTED_GRID_ID = "selected-grid-id";
	public static final String DASHBOARD_SELECTED_CELL = "selected-cell";
	public static final String DASHBOARD_COLOR_CHANGED = "color-changed";
	public static final String SERVICE_PIXEL_GRID_UPDATED = "cell-updated";
	
	public DashboardController(String registryAddress) {
		this.registryAddress = registryAddress;
	}
	
	public void launch() {
		Vertx.vertx().deployVerticle(this);
	}
	
	public void start(){	
		view = new DashboardView(pixelLocalViewModel, 800, 800);
		view.addPixelGridEventListener(this);
		
		var ebus = this.getVertx().eventBus();
		ebus.consumer(SELECTED_GRID_ID, this::selectedGridIdHandler); 
		ebus.consumer(DASHBOARD_SELECTED_CELL, this::selectedCellHandler); 
		ebus.consumer(DASHBOARD_COLOR_CHANGED, this::colorChangedHandler); 
		ebus.consumer(SERVICE_PIXEL_GRID_UPDATED, this::pixelColorChangedHandler); 

		view.displayBootingDialog();
	}
	
	/* events notified by the GUI */

	public void selectedGridId(String gridId) {
		var ebus = this.getVertx().eventBus();
		ebus.publish(SELECTED_GRID_ID, new JsonObject().put("gridId",gridId));
	}
	
	public void selectedCell(int x, int y) {
		var ebus = this.getVertx().eventBus();
		ebus.publish(DASHBOARD_SELECTED_CELL, new JsonObject().put("x",x).put("y", y));
	}

	public void colorChanged(int color) {
		var ebus = this.getVertx().eventBus();
		ebus.publish(DASHBOARD_COLOR_CHANGED, new JsonObject().put("color",color));
	}

	/* events notified by the service */
	
	public void pixelColorChanged(int x, int y, int color) {
		var ebus = this.getVertx().eventBus();
		ebus.publish(SERVICE_PIXEL_GRID_UPDATED, new JsonObject().put("x",x).put("y", y).put("color",color));
	}	

	/* handlers to manage events in the event loop */

	private void selectedGridIdHandler(Message<JsonObject> msg) {
		gridId = msg.body().getString("gridId");
		var lib = PixelArtServiceLib.getInstance();
		try {
			var registryURL = new URI(registryAddress).toURL();
			var registry = lib.getPixelGridRegistry(registryURL);			
			Future<String> gridAddress = registry.lookupPixelGrid(gridId);					
			gridAddress.onSuccess(addr -> {
				URL gridURL;
				try {
					gridURL = new URI(addr).toURL();
					proxy = lib.getPixelGrid(gridURL);
					proxy
					.createBrush()
					.onSuccess((brushId) -> {
						this.brushId = brushId;
						logger.log(Level.INFO, "Brush created: " + brushId);					
						proxy.subscribePixelGridEvents(this)
						.onSuccess(grid -> {
							logger.log(Level.INFO, "PixelGrid subscribed. ");	
							pixelLocalViewModel = new DashboardModel(grid);
							view.setGrid(pixelLocalViewModel);
							pixelLocalViewModel.addListener(view);
							view.display();
						});
					});
				} catch (MalformedURLException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void selectedCellHandler(Message<JsonObject> msg) {
		JsonObject obj = msg.body();
		var x = obj.getInteger("x");
		var y = obj.getInteger("y");
		proxy.moveBrushTo(brushId, y, x)
		.onSuccess(res -> {
			proxy.selectPixel(brushId);
		});					
	}

	public void colorChangedHandler(Message<JsonObject> msg) {
		int color = msg.body().getInteger("color");
		proxy.changeBrushColor(brushId, color)
		.onSuccess(res -> {
			view.setLocalBrushColor(color);
			logger.log(Level.INFO, "Color changed to: " + color);
		});
	}
	
	public void pixelColorChangedHandler(Message<JsonObject> msg) {
		JsonObject obj = msg.body();
		var x = obj.getInteger("x");
		var y = obj.getInteger("y");
		var color = obj.getInteger("color");
		logger.log(Level.INFO, "New event from service " + y + " " + x + " color: " + color);
		pixelLocalViewModel.set(x, y, color);
	}	
	


}

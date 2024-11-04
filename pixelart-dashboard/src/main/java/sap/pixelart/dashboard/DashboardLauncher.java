package sap.pixelart.dashboard;

import sap.pixelart.dashboard.controller.DashboardController;

public class DashboardLauncher {
	
	public final static String PIXEL_GRID_REGISTRY_ADDRESS = "http://localhost:9000";
	
	public static void main(String[] args) throws Exception {

		var controller = new DashboardController(PIXEL_GRID_REGISTRY_ADDRESS);
		controller.launch();
	}
}

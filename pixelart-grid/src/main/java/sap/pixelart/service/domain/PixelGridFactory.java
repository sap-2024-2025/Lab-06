package sap.pixelart.service.domain;

import sap.ddd.Factory;

public class PixelGridFactory implements Factory {
	
	private static PixelGridFactory instance;
	
	public static PixelGridFactory getInstance() {
		if (instance == null) {
			instance = new PixelGridFactory();
		}
		return instance;
	}
	
	private PixelGridFactory() {}

	public PixelGrid makePixelGrid(String id, int numCol, int numRow) {
		return new PixelGrid(id, numCol, numRow);
	}
}

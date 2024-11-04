package sap.pixelart.library;

import java.net.URL;

/**
 * 
 * Library for interacting with the PixelArt service
 * 
 * - it is a singleton factory
 * 
 * @author aricci
 *
 */
public class PixelArtServiceLib {

	private static PixelArtServiceLib instance;
	
	private PixelArtServiceLib() {
	}
	
	static public PixelArtServiceLib getInstance() {
		synchronized (PixelArtServiceLib.class) {
			if (instance == null) {
				instance = new PixelArtServiceLib();
			}
			return instance;
		}
	}

	
	public PixelGridRegistryAsyncAPI getPixelGridRegistry(URL registryAddress) {
		PixelGridRegistryAsyncAPI registryProxy = new PixelGridRegistryProxy(registryAddress);
		return registryProxy;
	}
	
	public PixelGridAsyncAPI getPixelGrid(URL gridAddress) {
		PixelGridProxy serviceProxy = new PixelGridProxy(gridAddress);
		return serviceProxy;
	}
	
	
}

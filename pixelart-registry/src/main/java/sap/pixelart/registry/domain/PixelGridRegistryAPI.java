package sap.pixelart.registry.domain;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface PixelGridRegistryAPI  {
	
	void registerPixelGrid(String name, URL address);
	
	Optional<URL> lookupPixelGrid(String name);

	List<String> getPixelGridsId();
	
}

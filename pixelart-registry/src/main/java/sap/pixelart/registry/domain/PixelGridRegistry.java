package sap.pixelart.registry.domain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import sap.ddd.Aggregate;

public class PixelGridRegistry implements PixelGridRegistryAPI, Aggregate<String> {

	private ConcurrentHashMap<String, URL> registry;
	private String registryId;
	
	public PixelGridRegistry(String registryId) {
		registry = new ConcurrentHashMap<>();
		this.registryId = registryId;
	}
	
	public void registerPixelGrid(String name, URL address) {
		registry.put(name, address);
	}
	
	public Optional<URL> lookupPixelGrid(String name) {
		var el = registry.get(name);
		if (el == null) {
			return Optional.empty();
		} else {
			return Optional.of(el);
		}
	}

	@Override
	public List<String> getPixelGridsId() {
		var l = new ArrayList<String>();
		for (var el: registry.entrySet()) {
			l.add(el.getKey());
		}
		return l;
	}

	@Override
	public String getId() {
		return registryId;
	}

}

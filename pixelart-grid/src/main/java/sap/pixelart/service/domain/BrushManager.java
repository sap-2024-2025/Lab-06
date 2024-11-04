package sap.pixelart.service.domain;

import java.util.Collection;
import java.util.HashMap;
import sap.ddd.Aggregate;
import sap.ddd.Entity;

public class BrushManager implements Entity<String>{

	private HashMap<String,Brush> brushes = new java.util.HashMap<>();
	private String id;
	
    public BrushManager(String id) {
    	this.id = id;
    	brushes = new java.util.HashMap<>();
    }
    
    public void addBrush(String id, Brush brush) {
        brushes.put(id, brush);
    }

    public Brush getBrush(String id) {
        return brushes.get(id);
    }
    
    public void removeBrush(String id) {
    	brushes.remove(id);	
    }
    
    public Collection<String> getBrushesId() {
    	return brushes.keySet();
    }

    public String getId() {
    	return id;
    }
}

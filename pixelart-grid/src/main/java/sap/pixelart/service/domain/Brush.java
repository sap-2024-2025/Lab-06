package sap.pixelart.service.domain;

import sap.ddd.Entity;

public class Brush implements Entity<String> {
    private int x, y;
    private int color;
    private String id;

    public Brush(String id, int x, int y, int color) {
    	this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void updatePosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    // write after this getter and setters
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getColor(){
        return this.color;
    }
    public void setColor(int color){
        this.color = color;
    }

	@Override
	public String getId() {
		return id;
	}
}

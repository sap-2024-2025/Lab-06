package sap.pixelart.service.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sap.ddd.Aggregate;

public class PixelGrid implements Aggregate<String>{
	
	private String id;
	private final int nRows;
	private final int nColumns;
	private final int[][] grid;
    private final List<PixelGridEventObserver> pixelListeners;
	
	PixelGrid(String id, int nRows, int nColumns) {
		this.id = id;
		this.nRows = nRows;
		this.nColumns = nColumns;
		grid = new int[nRows][nColumns];
		pixelListeners = new ArrayList<>();
	}

	public void clear() {
		for (int i = 0; i < nRows; i++) {
			Arrays.fill(grid[i], 0);
		}
	}
	
	public void set(final int x, final int y, final int color) {
		grid[y][x] = color;
		pixelListeners.forEach(l -> l.pixelColorChanged(x, y, color));

	}
	
	public int get(int x, int y) {
		return grid[y][x];
	}
	
	public int getNumRows() {
		return this.nRows;
	}
	

	public int getNumColumns() {
		return this.nColumns;
	}
	
	public String getId() {
		return id;
	}
    public void addPixelGridEventListener(PixelGridEventObserver l) { 
    	pixelListeners.add(l); 
    }
	
}

package sap.pixelart.dashboard.view;

public interface DashboardViewListener {

	void selectedCell(int x, int y);

	void selectedGridId(String gridId);	
	
	void colorChanged(int color);
	
}

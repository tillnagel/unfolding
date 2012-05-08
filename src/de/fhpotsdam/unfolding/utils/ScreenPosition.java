package de.fhpotsdam.unfolding.utils;


public class ScreenPosition {
	
	public int x;
	public int y;
	
	public ScreenPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public ScreenPosition(float[] xy) {
		this.x = Math.round(xy[0]);
		this.y = Math.round(xy[1]);
	}
}

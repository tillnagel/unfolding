package de.fhpotsdam.unfolding.utils;


public class ScreenPosition {
	
	public float x;
	public float y;
	
	public ScreenPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public ScreenPosition(float[] xy) {
		this.x = Math.round(xy[0]);
		this.y = Math.round(xy[1]);
	}
}

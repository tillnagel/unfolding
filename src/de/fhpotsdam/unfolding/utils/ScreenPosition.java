package de.fhpotsdam.unfolding.utils;

import processing.core.PVector;

/**
 * A ScreenPosition just a position on the screen. x and y are
 * pixel coordinates in a given map.
 *
 * ScreenPosition offers all the convenient functions of PVector.
 *
 * Map has several functions to translate ScreenPosition to Location
 * and vice versa.
 */
public class ScreenPosition extends PVector{
	private static final long serialVersionUID = 7019567250811315361L;

	public ScreenPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public ScreenPosition(float[] xy) {
		this.x = Math.round(xy[0]);
		this.y = Math.round(xy[1]);
	}
	
	public ScreenPosition(float[] xy, boolean exact) {
		this.x = xy[0];
		this.y = xy[1];
	}
}

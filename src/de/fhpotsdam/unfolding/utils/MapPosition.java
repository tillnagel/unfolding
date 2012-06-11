package de.fhpotsdam.unfolding.utils;

import processing.core.PVector;

@SuppressWarnings("serial")
public class MapPosition extends PVector{
	public MapPosition() {
	}
	
	public MapPosition(float []xy){
		x = xy[0];
		y = xy[1];
	}
}

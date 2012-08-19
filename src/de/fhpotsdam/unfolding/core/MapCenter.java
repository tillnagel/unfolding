package de.fhpotsdam.unfolding.core;

import processing.core.PVector;

/**
 * Internal representaion of the center of the map.
 * 
 * You probably won't use this class.
 */
public class MapCenter {

	public Coordinate coordinate;
	public PVector point;

	public MapCenter(Coordinate coordinate, PVector point) {
		this.coordinate = coordinate;
		this.point = point;
	}
}
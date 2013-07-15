package de.fhpotsdam.unfolding.utils;

import processing.core.PVector;

/**
 * Class used to store positions on a specific map. Used internally by some markers. These are neither geo-locations,
 * nor screen positions, but the position on the map object in screen coordinate system.
 */
@SuppressWarnings("serial")
public class MapPosition extends PVector {
	public MapPosition() {
	}

	public MapPosition(float[] xy) {
		x = xy[0];
		y = xy[1];
	}
}

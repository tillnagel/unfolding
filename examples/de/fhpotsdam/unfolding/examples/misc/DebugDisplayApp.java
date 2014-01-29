package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows basic information about the map. Can be used for debugging purposes.
 */
public class DebugDisplayApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;

	public void setup() {
		size(1024, 768, P2D);

		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Create debug display (optional: specify position and size)
		debugDisplay = new DebugDisplay(this, map);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}
}

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

	public void settings() {
		size(1024, 768, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		MapUtils.createDefaultEventDispatcher(this, map);

		// Create debug display (optional: specify position and size)
		debugDisplay = new DebugDisplay(this, map);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { DebugDisplayApp.class.getName() });
	}
}

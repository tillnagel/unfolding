package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows basic information about the map. Can be used for debugging purposes.
 */
public class MultiDebugDisplayApp extends PApplet {

	UnfoldingMap map1;
	UnfoldingMap map2;
	DebugDisplay debugDisplay1;
	DebugDisplay debugDisplay2;

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);

		map1 = new UnfoldingMap(this, "Map 1", 0, 0, 512, height);
		map1.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		map2 = new UnfoldingMap(this, "Map 2", 512, 0, 512, height, true, false, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map1, map2);

		// Create debug displays
		debugDisplay1 = new DebugDisplay(this, map1, 10, 10);
		debugDisplay2 = new DebugDisplay(this, map2, 522, 10);
	}

	public void draw() {
		map1.draw();
		debugDisplay1.draw();

		map2.draw();
		debugDisplay2.draw();
	}
}

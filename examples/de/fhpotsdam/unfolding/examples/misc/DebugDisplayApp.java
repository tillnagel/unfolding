package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
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
		size(1024, 768, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(loadFont("Miso-Light-12.vlw"), 20);
		debugDisplay = new DebugDisplay(this, map.mapDisplay, 10, 450, 300, 200);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}
}

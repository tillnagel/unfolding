package de.fhpotsdam.unfolding.examples.data;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays two markers, by simply updating their position based on their locations. 
 * The locations are mapped according to current map transformation.
 * 
 * For usage of MarkerManager look into other examples, e.g. {@link InfoMarkerApp}.
 */
public class SimpleMarkerApp extends PApplet {

	Map map;

	Location locationBerlin = new Location(52.5f, 13.4f);
	Location locationLondon = new Location(51.5f, 0f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);

		noStroke();
	}

	public void draw() {
		background(0);
		map.draw();

		// Draw the locations on the screen position according to their geo-locations.
		
		float xyBerlin[] = map.mapDisplay.getScreenPositionFromLocation(locationBerlin);
		fill(0, 200, 0, 100);
		ellipse(xyBerlin[0], xyBerlin[1], 20, 20);

		float xyLondon[] = map.mapDisplay.getScreenPositionFromLocation(locationLondon);
		fill(200, 0, 0, 100);
		ellipse(xyLondon[0], xyLondon[1], 20, 20);
	}

}

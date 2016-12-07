package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays two dots, by simply updating their position based on their locations. Look into Unfolding's Marker
 * mechanism, if you want to use interactivity, load data or do anything else more than simply showing dots.
 * 
 * <p>
 * Note, that this simple mechanism only works for full-sized maps (i.e. the markers are shown off-map, too). For usage
 * of MarkerManager look into other examples, e.g. {@link SimpleMarkerManagerApp}.
 * </p>
 */
@SuppressWarnings("serial")
public class SimpleNonMarkerApp extends PApplet {

	UnfoldingMap map;

	Location locationBerlin = new Location(52.5f, 13.4f);
	Location locationLondon = new Location(51.5f, 0f);

	public void settings() {
		size(400, 400, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { SimpleNonMarkerApp.class.getName() });
	}

	public void setup() {
		noStroke();

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		// Draws locations on screen positions according to their geo-locations.

		// Fixed-size element
		ScreenPosition xyBerlin = map.getScreenPosition(locationBerlin);
		fill(0, 200, 0, 100);
		ellipse(xyBerlin.x, xyBerlin.y, 20, 20);

		// Zoom dependent element size
		ScreenPosition xyLondon = map.getScreenPosition(locationLondon);
		fill(200, 0, 0, 100);
		float s = map.getZoom();
		ellipse(xyLondon.x, xyLondon.y, s, s);
	}

}

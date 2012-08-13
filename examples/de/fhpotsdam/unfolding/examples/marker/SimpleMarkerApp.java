package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple marker display, without the use of MarkerManager.
 * 
 * Conversion between geo-location and screen position is done via the marker,
 * but drawing the markers is done by this application itself.
 * 
 * Easiest way of drawing own styled markers.
 */
@SuppressWarnings("serial")
public class SimpleMarkerApp extends PApplet {

	UnfoldingMap map;

	SimplePointMarker markerBerlin;
	SimplePointMarker markerLondon;

	public void setup() {
		size(800, 400, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.setTweening(true);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);

		// Create Markers from Locations
		Location locationBerlin = new Location(52.5f, 13.4f);
		Location locationLondon = new Location(51.5f, 0f);

		markerBerlin = new SimplePointMarker(locationBerlin);
		markerLondon = new SimplePointMarker(locationLondon);
	}

	public void draw() {
		background(70);

		map.draw();

		// Draws Markers on screen positions according to their geo-locations.

		// Fixed-size marker
		ScreenPosition posBerlin = markerBerlin.getScreenPosition(map);
		fill(0, 200, 0, 100);
		ellipse(posBerlin.x, posBerlin.y, 20, 20);

		// Zoom dependent marker size
		ScreenPosition posLondon = markerLondon.getScreenPosition(map);
		fill(200, 0, 0, 100);
		float s = map.getZoom();
		ellipse(posLondon.x, posLondon.y, s, s);
	}
}

package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple marker display, without the use of MarkerManager.
 * 
 * Conversion between geo-location and screen position is done via the marker, but drawing the markers is done by this
 * application itself. Easiest way of drawing own styled markers.
 * 
 */
@SuppressWarnings("serial")
public class SimpleMarkerApp extends PApplet {

	UnfoldingMap map;

	SimpleMarker markerBerlin;
	SimpleMarker markerLondon;

	public void setup() {
		size(800, 400, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.setTweening(true);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);

		markerBerlin = new SimpleMarker(new Location(52.5f, 13.4f));
		markerBerlin.radius = 10;
		markerLondon = new SimpleMarker(new Location(51.5f, 0f));
	}

	public void draw() {
		background(70);

		map.draw();

		// Draws locations on screen positions according to their geo-locations.

		// Fixed-size marker
		ScreenPosition posBerlin = markerBerlin.getScreenPosition(map);
		fill(0, 200, 0, 100);
		ellipse(posBerlin.x, posBerlin.y, 20, 20);
		// REVISIT draw directly? (till: I vote against this, as it's an internal mechanism)
		// markerBerlin.drawNOW(map);

		// Zoom dependent marker size
		ScreenPosition posLondon = markerLondon.getScreenPosition(map);
		fill(200, 0, 0, 100);
		float s = map.getZoom();
		ellipse(posLondon.x, posLondon.y, s, s);
	}
}

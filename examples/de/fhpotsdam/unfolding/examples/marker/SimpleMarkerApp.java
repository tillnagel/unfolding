package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple custom marker display, without the use of MarkerManager.
 * 
 * <p>
 * <em>Use only if you want to customize more than colors AND are not familiar with writing own classes.</em><br/>
 * If you want to only customize colors, use Unfolding's marker manager (see {@link SimpleMarkerManagerApp}).<br/>
 * If you want to have complete control, but also want to use the marker manager, write your own marker class (see {@link }).
 * </p> 
 * 
 * <p>
 * Here, conversion between geo-location and screen position is done via the marker, but drawing the markers is done by this
 * application itself. This is the easiest way of drawing own styled markers. A more advanced way is to create an own
 * Marker class with custom style, where all the position handling can be done via the internal marker mechanism. See
 * tutorials for an explanation of the differences.
 * </p>
 */
@SuppressWarnings("serial")
public class SimpleMarkerApp extends PApplet {

	UnfoldingMap map;

	SimplePointMarker markerBerlin;
	SimplePointMarker markerLondon;

	public void setup() {
		size(800, 400);
		smooth();

		map = new UnfoldingMap(this);
		// map.setTweening(true);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);

		// Create Markers from Locations
		Location locationBerlin = new Location(52.5f, 13.4f);
		Location locationLondon = new Location(51.5f, 0f);

		markerBerlin = new SimplePointMarker(locationBerlin);
		markerLondon = new SimplePointMarker(locationLondon);

		PFont font = createFont("serif-bold", 12);
		textFont(font);
	}

	public void draw() {
		background(70);

		map.draw();

		// Draws Markers on screen positions according to their geo-locations.

		// Fixed-size marker
		ScreenPosition posBerlin = markerBerlin.getScreenPosition(map);
		strokeWeight(1);
		stroke(0, 100);
		fill(0, 200, 0, 100);
		ellipse(posBerlin.x, posBerlin.y, 20, 20);

		ScreenPosition posLondon = markerLondon.getScreenPosition(map);
		strokeWeight(12);
		stroke(200, 0, 0, 200);
		strokeCap(SQUARE);
		noFill();
		// Zoom dependent marker size
		// float s = map.getZoom();
		float s = 44;
		arc(posLondon.x, posLondon.y, s, s, -PI * 0.9f, -PI * 0.1f);
		arc(posLondon.x, posLondon.y, s, s, PI * 0.1f, PI * 0.9f);
		fill(0);
		text("London", posLondon.x - textWidth("London") / 2, posLondon.y + 4);
	}
}

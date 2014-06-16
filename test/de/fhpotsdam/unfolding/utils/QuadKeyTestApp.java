package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;

/**
 * Tests quad key conversions.
 */
public class QuadKeyTestApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600);

		map = new UnfoldingMap(this);
		Location location = new Location(52.52, 13.38);

		Coordinate coord = map.mapDisplay.getMapProvider().locationCoordinate(location).zoomTo(14);
		String quadKey = Microsoft.toQuadKey(coord);
		println("QuadKey: " + quadKey + " for location: " + location);

		String hereAPIUriString = "http://traffic.cit.api.here.com/traffic/6.1/flow.json?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg&quadkey=";
		hereAPIUriString += quadKey;

		String[] result = loadStrings(hereAPIUriString);
		for (String r : result) {
			println(r);
		}
	}

	public void draw() {
		// map.draw();
	}

}

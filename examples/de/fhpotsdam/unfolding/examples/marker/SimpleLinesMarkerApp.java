package de.fhpotsdam.unfolding.examples.marker;

import java.util.List;

/**
 * Shows a simple line marker between two locations.
 */
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;

public class SimpleLinesMarkerApp extends PApplet {

	UnfoldingMap map;
	SimpleLinesMarker connectionMarker;

	UnfoldingMap currentMap;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {

		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		currentMap = map;

		map = new UnfoldingMap(this);
		Location startLocation = new Location(32.2, 76.3);
		Location endLocation = new Location(53.35, -6.26);
		connectionMarker = new SimpleLinesMarker(startLocation, endLocation);
		//connectionMarker.addLocation(40.0f,  15.0f);
		MapUtils.createDefaultEventDispatcher(this, map);

		map.addMarker(connectionMarker);
	}

	public void draw() {
		map.draw();

		println(connectionMarker.getLocation());


		
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleLinesMarkerApp.class.getName() });
	}

}

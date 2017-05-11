package de.fhpotsdam.unfolding.examples.marker;

/**
 * Shows a simple line marker between two locations.
 */
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
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
		SimpleLinesMarker connectionMarker = new SimpleLinesMarker(startLocation, endLocation);
		MapUtils.createDefaultEventDispatcher(this, map);

		map.addMarker(connectionMarker);
	}

	public void draw() {
		map.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleLinesMarkerApp.class.getName() });
	}

}

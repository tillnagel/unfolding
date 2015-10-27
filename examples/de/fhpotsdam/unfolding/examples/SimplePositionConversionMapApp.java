package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.marker.SimpleMarkerManagerApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple map app showing how to convert screen position to geo-location, and vice versa.
 * 
 * For automatic conversion from geo-location to screen, take a look at Unfolding's marker mechanism.
 * Start at {@link SimpleMarkeApp} and {@link SimpleMarkerManagerApp}.
 */
public class SimplePositionConversionMapApp extends PApplet {

	UnfoldingMap map;
	
	public void settings() {
		size(800, 600, P2D);
	}
	
	public void setup() {
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(240);
		map.draw();

		fill(215, 0, 0, 100);
		// Shows latitude,longitude at mouse position
		Location location = map.getLocation(mouseX, mouseY);
		text("geo:" + location.toString(), mouseX, mouseY);

		// Shows marker at Berlin location
		Location loc = new Location(52.5f, 13.4f);
		ScreenPosition pos = map.getScreenPosition(loc);
		ellipse(pos.x, pos.y, 20, 20);

		String berlinDescription = "Berlin at pixel (" + (int) pos.x + "," + (int) pos.y + ")";
		text(berlinDescription, pos.x, pos.y);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { SimplePositionConversionMapApp.class.getName() });
	}
}

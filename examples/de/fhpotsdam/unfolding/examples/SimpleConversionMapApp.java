package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple map app showing how to convert screen position to geo-location, and vice versa.
 */
public class SimpleConversionMapApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);

		map.draw();

		noStroke();
		fill(215, 0, 0, 100);

		// Shows geo-location at mouse position
		Location location = map.getLocationFromScreenPosition(mouseX, mouseY);
		text(location.toString(), mouseX, mouseY);

		// Shows marker at Berlin location
		Location loc = new Location(52.5f, 13.4f);
		float xy[] = map.getScreenPositionFromLocation(loc);
		ellipse(xy[0], xy[1], 20, 20);
	}

}

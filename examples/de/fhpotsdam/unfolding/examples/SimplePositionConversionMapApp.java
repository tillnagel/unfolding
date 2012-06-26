package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple map app showing how to convert screen position to geo-location, and vice versa.
 */
public class SimplePositionConversionMapApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();

		fill(215, 0, 0, 100);

		// Shows geo-location at mouse position
		Location location = map.getLocation(mouseX, mouseY);
		text(location.toString(), mouseX, mouseY);
	
	
		// Shows marker at Berlin location
		Location loc = new Location(52.5f, 13.4f);
		ScreenPosition pos = map.getScreenPosition(loc);
		ellipse(pos.x, pos.y, 20, 20);
		
		
		String berlinDescription = "Berlin at pixel (" + (int)pos.x + "/" + (int)pos.y + ")"; 
		text(berlinDescription, pos.x, pos.y);
	}
	
	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimplePositionConversionMapApp" });
	}
}

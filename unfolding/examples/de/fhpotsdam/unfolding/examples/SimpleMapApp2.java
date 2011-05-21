package de.fhpotsdam.unfolding.examples;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleMapApp2 extends PApplet {

	public static Logger log = Logger.getLogger(SimpleMapApp2.class);

	Map map;
	DebugDisplay debugDisplay;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, 50, 50, 700, 500);
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map.mapDisplay, 0, 0, 250, 200);
	}

	public void draw() {
		background(0);

		map.draw();

		// Show location from mouse
		Location location = map.mapDisplay.getLocationFromScreenPosition(mouseX, mouseY);
		fill(215, 0, 0);
		text(location + "", mouseX, mouseY);
		
		// Shows marker on Berlin
		// Simple method: Does work correctly only for full-sized maps (not via marker mechanism)
		Location loc = new Location(52.5f, 13.4f);
		float xy[] = map.mapDisplay.getScreenPositionFromLocation(loc);
		float s = map.mapDisplay.innerScale;
		ellipse(xy[0], xy[1], s, s);

		debugDisplay.draw();
	}
}

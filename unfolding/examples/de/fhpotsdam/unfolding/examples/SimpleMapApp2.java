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

		map = new Map(this, "map1", 50, 50, 700, 500);
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map.mapDisplay, 0, 0, 250, 200);
	}

	public void draw() {
		background(0);

		map.draw();
		debugDisplay.draw();

		// Show location from mouse
		Location location = map.mapDisplay.getLocationFromScreenPosition(mouseX, mouseY);
		fill(215, 0, 0);
		text(location + "", mouseX, mouseY);

		Location loc = new Location(54f, 13.5f);
		float xy[] = map.mapDisplay.getScreenPositionFromLocation(loc);
		float s = map.mapDisplay.innerScale;
		ellipse(xy[0], xy[1], s, s);
	}
}

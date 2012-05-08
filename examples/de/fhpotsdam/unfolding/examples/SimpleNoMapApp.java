package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleNoMapApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.updateMap();
		// map.draw();

		Location loc = new Location(52.5f, 13.4f);
		float xy[] = map.getScreenPositionFromLocation(loc);
		ellipse(xy[0], xy[1], 20, 20);
	}

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleNoMapApp" });
	}
}

package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleBackgroundMapApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, 50, 50, 700, 500);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		map.setBackgroundColor(color(60,  70, 10));

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(30,  70, 10);
		map.draw();
	}


}

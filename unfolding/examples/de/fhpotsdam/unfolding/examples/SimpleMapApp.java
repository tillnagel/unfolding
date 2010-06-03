package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;

public class SimpleMapApp extends PApplet {

	EventDispatcher eventDispatcher;
	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		eventDispatcher = new EventDispatcher();

		map = new Map(this, "map1");

		eventDispatcher.register(map, "pan");
		eventDispatcher.register(map, "zoom");
	}

	public void draw() {
		background(0);

		map.draw();
	}
}

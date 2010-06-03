package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleMultiMapApp extends PApplet {

	Map map1;
	Map map2;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map1 = new Map(this, "map1", 10, 10, 385, 580);
		map2 = new Map(this, "map2", 405, 10, 385, 580);

		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
	}

}

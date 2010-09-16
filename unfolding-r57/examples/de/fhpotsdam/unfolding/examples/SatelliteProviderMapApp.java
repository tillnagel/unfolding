package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SatelliteProviderMapApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1", 0, 0, width, height, true, false, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);

		map.draw();
	}

}

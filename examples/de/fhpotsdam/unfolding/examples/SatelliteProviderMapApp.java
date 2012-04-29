package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SatelliteProviderMapApp extends PApplet {

	Map map;

	public void setup() {
		size(400, 400, GLConstants.GLGRAPHICS);

		map = new Map(this, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		
		map.draw();
	}

}


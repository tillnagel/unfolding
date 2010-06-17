package de.fhpotsdam.unfolding.examples;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleMapApp extends PApplet {

	public static Logger log = Logger.getLogger(SimpleMapApp.class);

	Map map;
	DebugDisplay debugDisplay;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1");
		MapUtils.createDefaultEventDispatcher(this, map);
		
		debugDisplay = new DebugDisplay(this, map.mapDisplay);
	}

	public void draw() {
		background(0);

		map.draw();

		debugDisplay.draw();
	}

}

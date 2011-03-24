package de.fhpotsdam.unfolding.examples;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleMapApp extends PApplet {

	public static Logger log = Logger.getLogger(SimpleMapApp.class);

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);

		map.draw();
	}
}

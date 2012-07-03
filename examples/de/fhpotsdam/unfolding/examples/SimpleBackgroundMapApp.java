package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Show how
 */
public class SimpleBackgroundMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		// the map is smaller than PApplet window
		map = new UnfoldingMap(this, 50, 50, 700, 500);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		
		// the background of he map gets a color. you need to zoom out a lot to see it.
		map.setBackgroundColor(color(60,  70, 10));

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		// the part that is not the map gets a different color
		background(30,  70, 10);
		map.draw();
	}
	
	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleBackgroundMapApp" });
	}
}

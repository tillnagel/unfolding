package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows how to set the background color of the map where no tiles have been loaded yet, or where not tiles exist.
 * 
 * Zoom out quickly to see the behaviour.
 */
public class SimpleBackgroundMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// background color of the map
		map.setBackgroundColor(color(60, 70, 10));
	}

	public void draw() {
		// Outer area the map gets a different color
		background(30, 70, 10);
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleBackgroundMapApp" });
	}
}

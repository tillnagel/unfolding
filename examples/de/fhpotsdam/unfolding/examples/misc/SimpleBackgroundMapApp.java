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

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, 50, 50, 700, 500);
		map.zoomAndPanTo(3, new Location(52.5f, 13.4f));
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
		PApplet.main(new String[] { SimpleBackgroundMapApp.class.getName() });
	}
}

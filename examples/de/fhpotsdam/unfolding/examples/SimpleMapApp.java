package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
public class SimpleMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, P2D);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleMapApp.class.getName() });
	}
}

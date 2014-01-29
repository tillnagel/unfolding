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
		size(1024, 768, P2D);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(6.316667f, 5.6f), 6);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleMapApp.class.getName() });
	}
}

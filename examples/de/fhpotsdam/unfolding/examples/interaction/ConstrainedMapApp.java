package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This map is constrained in both pan and zoom interactions. Users are allowed to only pan within a specified radial
 * area, and to zoom within specific zoom levels.
 */
public class ConstrainedMapApp extends PApplet {

	UnfoldingMap map;

	Location centerLocation = new Location(1.359f, 103.816f);
	float maxPanningDistance = 30;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(12, new Location(centerLocation));
		map.setPanningRestriction(centerLocation, maxPanningDistance);
		map.setZoomRange(12, 15);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { ConstrainedMapApp.class.getName() });
	}

}

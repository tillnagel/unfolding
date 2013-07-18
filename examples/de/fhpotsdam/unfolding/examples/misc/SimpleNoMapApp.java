package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.marker.multimarker.MultiMarkerApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple example using map interaction and geo-location conversion without displaying a map.
 */
public class SimpleNoMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(50f, 12f), 4);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.updateMap();

		beginShape();
		for (Location location : MultiMarkerApp.getFranceShapeLocations()) {
			ScreenPosition pos = map.getScreenPosition(location);
			vertex(pos.x, pos.y);
		}
		endShape();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleNoMapApp" });
	}
}

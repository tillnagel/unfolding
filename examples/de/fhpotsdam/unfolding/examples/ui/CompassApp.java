package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.ui.CompassUI;

/**
 * Simple map example using CompassUI. 
 * 
 * Use r / l for map rotation.
 */

public class CompassApp extends PApplet {

	UnfoldingMap map;
	CompassUI compass;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
//		MapUtils.createDefaultEventDispatcher(this, map);

		compass = new CompassUI(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
		compass.draw();
	}

	public void keyPressed() {
		if (key == 'r')
			map.rotate(0.1f);
		if (key == 'l')
			map.rotate(-0.1f);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.CompassApp" });
	}
}
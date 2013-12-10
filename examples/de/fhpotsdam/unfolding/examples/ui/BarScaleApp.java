package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.ui.BarScaleUI;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple map example with a styled bar scale (at the right top).
 */

public class BarScaleApp extends PApplet {

	UnfoldingMap map;
	BarScaleUI barScale;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// add a bar scale to your map
		barScale = new BarScaleUI(this, map, 700, 20);

		// optionally style your bar scale
		PFont myFont = createFont("Monospaced", 12);
		barScale.setStyle(color(60, 120), 6, -2, myFont);
	}

	public void draw() {
		background(0);
		map.draw();

		barScale.draw();
	}

	public void keyPressed() {
		if (key == '+')
			map.zoomIn();
		if (key == '-')
			map.zoomOut();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.BarScaleApp" });
	}

}
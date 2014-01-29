package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import processing.core.PImage;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.ui.CompassUI;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple map example using CompassUI with a custom image.
 * 
 * Use r / l for map rotation.
 */

public class StyledCompassApp extends PApplet {

	UnfoldingMap map;
	CompassUI compass;
	PImage compassImg;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		compassImg = loadImage("ui/compass_grey.png");
		compass = new CompassUI(this, map, compassImg, 700, 100);
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
		if (key == 's')
			compass.setScale(compass.getScale() * 0.95f);
		if (key == 'S')
			compass.setScale(compass.getScale() * 1.05f);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.StyledCompassApp" });
	}

}
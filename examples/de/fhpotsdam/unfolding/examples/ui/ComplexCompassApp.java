package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import processing.core.PImage;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.ui.CompassUI;

public class ComplexCompassApp extends PApplet {

	UnfoldingMap map;
	CompassUI compass;
	PImage compassImg;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		compass = new CompassUI(this, map);

		compassImg = loadImage("compass_grey.png");
		compass = new CompassUI(this, map.mapDisplay, compassImg, 700, 100);
	}

	public void draw() {
		background(0);
		map.draw();
		compass.draw();
	}

	public void keyPressed() {
		if (key == 'a')
			map.rotate(0.1f);
		if (key == 'd')
			map.rotate(-0.1f);
		if (key == ' ')
			compass.setScale(compass.getScale() * 0.95f);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.ComplexCompassApp" });
	}

}
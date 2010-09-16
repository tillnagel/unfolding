package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import processing.core.PVector;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class RotatableMapApp extends PApplet {

	Map map;
	DebugDisplay debugDisplay;

	PVector rotateCenter = new PVector(350, 250);
	Location location = new Location(51.50939f, -0.11820f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1", 0, 0, 700, 500);
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(loadFont("Miso-Light-12.vlw"), 20);

		debugDisplay = new DebugDisplay(this, map.mapDisplay, 10, 450, 300, 200);
	}

	public void draw() {
		background(0);

		map.draw();
		debugDisplay.draw();

		float[] xy = map.mapDisplay.getScreenPositionFromLocation(location);
		stroke(255, 0, 0);
		noFill();
		ellipse(xy[0], xy[1], 10, 10);
	}

	public void keyPressed() {
		rotateCenter = new PVector(mouseX, mouseY);
		map.mapDisplay.setTransformationCenter(rotateCenter);

		if (key == 'r') {
			map.rotate(-PI / 8);
		} else if (key == 'l') {
			map.rotate(PI / 8);
		}
	}

}

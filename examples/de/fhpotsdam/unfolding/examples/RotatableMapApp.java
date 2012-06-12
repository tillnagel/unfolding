package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import processing.core.PVector;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Application to compare the two different map rotation styles.
 * - Inner-rotate transforms the map itself, and works with all renderers.
 * - Outer-rotate transforms the map container, and works with GLGraphics only.
 * 
 * Use r and l to inner rotate clock-wise (right), and counter-clock-wise (left), and R and L to
 * outer rotate.
 */
public class RotatableMapApp extends PApplet {

	Map map;
	DebugDisplay debugDisplay;

	PVector rotateCenter = new PVector(350, 250);
	Location location = new Location(51.50939f, -0.11820f);

	boolean fullRotatable = true;

	public void setup() {
		if (fullRotatable) {
			size(800, 600, GLConstants.GLGRAPHICS);
		} else {
			size(800, 600);
		}

		map = new Map(this, "map1", 50, 50, 700, 500);
		map.setTweening(false);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(loadFont("Miso-Light-12.vlw"), 20);

		debugDisplay = new DebugDisplay(this, map.mapDisplay, 10, 450, 300, 200);
	}

	public void draw() {
		background(0);

		map.draw();
		debugDisplay.draw();

		ScreenPosition pos = map.mapDisplay.getScreenPosition(location);
		stroke(255, 0, 0);
		noFill();
		ellipse(pos.x, pos.y, 10, 10);
	}

	public void keyPressed() {
		rotateCenter = new PVector(mouseX, mouseY);
		map.mapDisplay.setTransformationCenter(rotateCenter);

		// Inner rotate (i.e. map) works with both, P2D and GLGraphics
		if (key == 'r') {
			map.rotate(-PI / 8);
		} else if (key == 'l') {
			map.rotate(PI / 8);
		}

		// Outer rotate (i.e. map container) only works with GLGraphics offscreen buffer
		if (key == 'R') {
			map.outerRotate(-PI / 8);
		} else if (key == 'L') {
			map.outerRotate(PI / 8);
		}
	}
	
	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.RotatableMapApp" });
	}
}

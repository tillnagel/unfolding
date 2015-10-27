package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Application to compare the two different map rotation styles. Inner-rotate transforms the map itself, and works with
 * all renderers. Outer-rotate transforms the map container, and works with GLGraphics only.
 * 
 * Use r and l to inner rotate clock-wise (right), and counter-clock-wise (left), and respectively R and L to outer
 * rotate. Note, in this example the map rotates around the current mouse pointer.
 */
public class RotatableMapApp extends PApplet {

	UnfoldingMap map;

	PVector rotateCenter = new PVector(350, 250);
	Location location = new Location(51.50939f, -0.11820f);

	boolean fullRotatable = true;
	
	public void settings() {
		if (fullRotatable) {
			size(800, 600, P2D);
		} else {
			size(800, 600);
		}
	}
	
	public void setup() {
		map = new UnfoldingMap(this, "map1", 50, 50, 700, 500);
		map.setTweening(false);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);

		map.draw();

		ScreenPosition pos = map.mapDisplay.getScreenPosition(location);
		stroke(255, 0, 0);
		noFill();
		ellipse(pos.x, pos.y, 10, 10);
	}

	public void keyPressed() {
		rotateCenter = new PVector(mouseX, mouseY);

		// Inner rotate (i.e. map) works with both, P2D and GLGraphics
		map.mapDisplay.setInnerTransformationCenter(rotateCenter);
		if (key == 'r') {
			map.rotate(-PI / 8);
		} else if (key == 'l') {
			map.rotate(PI / 8);
		}

		// Outer rotate (i.e. map container) only works with GLGraphics offscreen buffer
		map.mapDisplay.setTransformationCenter(rotateCenter);
		if (key == 'R') {
			map.outerRotate(-PI / 8);
		} else if (key == 'L') {
			map.outerRotate(PI / 8);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.misc.RotatableMapApp" });
	}
}

package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import processing.core.PVector;

/** 
 * Simple app to test convex hull algorithm.
 * 
 * Click anywhere to add vertices. The convex hull is updated.
 *
 */
public class ConvexHullApp extends PApplet {

	ConvexHull convexHull;

	public void setup() {
		size(500, 500);
		background(255);
		smooth();
		
		convexHull = new ConvexHull(this);
		convexHull.showDebugPoints = true;
	}

	public void draw() {
		background(255);
		convexHull.draw();
	}

	public void mousePressed() {
		convexHull.addPoint(new PVector(mouseX, mouseY));
	}

	public void keyPressed() {
		if (key == BACKSPACE) {
			convexHull.clearPoints();
		}
	}

}

package de.fhpotsdam.unfolding.overviewdetail;

import de.fhpotsdam.unfolding.examples.overviewdetail.connection.ConvexHull;
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
		convexHull.addPoint(new PVector(random(width), random(height)));
		convexHull.addPoint(new PVector(random(width), random(height)));
		convexHull.addPoint(new PVector(random(width), random(height)));
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

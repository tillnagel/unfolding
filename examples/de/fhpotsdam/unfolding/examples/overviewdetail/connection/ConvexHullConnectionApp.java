package de.fhpotsdam.unfolding.examples.overviewdetail.connection;

import processing.core.PApplet;

public class ConvexHullConnectionApp extends PApplet {

	OverviewPlusDetailConnection convexHullConnection;

	public void setup() {
		size(500, 500);
		background(255);
		smooth();

		convexHullConnection = new ConvexHullConnection(this);
		convexHullConnection.setDetailSize(150, 150);
		convexHullConnection.setOverviewSize(50, 50);
	}

	public void draw() {
		background(255);
		convexHullConnection.draw();
	}

	public void mouseMoved() {
		convexHullConnection.setOverviewPosition(mouseX, mouseY);
	}

	public void mouseDragged() {
		convexHullConnection.setDetailPosition(mouseX, mouseY);
	}

}

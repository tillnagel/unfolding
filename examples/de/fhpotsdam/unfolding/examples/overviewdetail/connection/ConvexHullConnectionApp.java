package de.fhpotsdam.unfolding.examples.overviewdetail.connection;

import processing.core.PApplet;

public class ConvexHullConnectionApp extends PApplet {

	OverviewPlusDetailConnection convexHullConnection;
	
	public void settings() {
		size(500, 500, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { ConvexHullConnectionApp.class.getName() });
	}
	
	public void setup() {
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

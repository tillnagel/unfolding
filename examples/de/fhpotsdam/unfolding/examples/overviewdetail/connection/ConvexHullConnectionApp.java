package de.fhpotsdam.unfolding.examples.overviewdetail.connection;

import processing.core.PApplet;

public class ConvexHullConnectionApp extends PApplet {

	private OverviewPlusDetailConnection convexHullConnection;

	@Override
	public void settings() {
		size(500, 500);
	}

	@Override
	public void setup() {
		background(255);
		smooth();
		convexHullConnection = new ConvexHullConnection(this);
		convexHullConnection.setDetailSize(150, 150);
		convexHullConnection.setOverviewSize(50, 50);
	}

	@Override
	public void draw() {
		background(255);
		convexHullConnection.draw();
	}

	@Override
	public void mouseMoved() {
		convexHullConnection.setOverviewPosition(mouseX, mouseY);
	}

	@Override
	public void mouseDragged() {
		convexHullConnection.setDetailPosition(mouseX, mouseY);
	}

	public static void main(String args[]) {
		PApplet.main(new String[]{ConvexHullConnectionApp.class.getName()});
	}

}

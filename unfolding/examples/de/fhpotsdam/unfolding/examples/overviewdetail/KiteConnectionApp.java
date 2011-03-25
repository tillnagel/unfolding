package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;

public class KiteConnectionApp extends PApplet {

	KiteConnection kiteConnection;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		kiteConnection = new KiteConnection(this);
	}

	public void draw() {
		background(255);

		kiteConnection.draw();
		kiteConnection.drawDebug();
	}

	public void mouseMoved() {
		kiteConnection.setPosition(mouseX, mouseY);
	}

	public void mouseClicked() {
		kiteConnection.setDestination(mouseX, mouseY);
	}

}

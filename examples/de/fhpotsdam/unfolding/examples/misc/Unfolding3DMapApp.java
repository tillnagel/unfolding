package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

public class Unfolding3DMapApp extends PApplet {

	UnfoldingMap map;
	float rotateX = 0.9f;
	float rotateZ = (float) 0;
	float rotateVelocityZ = 0.003f;

	public void settings() {
		size(800, 600, P3D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(4, new Location(51.5, 0));
	}

	public void draw() {
		background(40);

		translate(width / 2, height / 3, 0);
		rotateX(rotateX);
		rotateZ(rotateZ);
		translate(-map.getWidth() / 2, -map.getHeight() / 2);
		map.draw();

		rotateZ += rotateVelocityZ;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Unfolding3DMapApp.class.getName() });
	}

}

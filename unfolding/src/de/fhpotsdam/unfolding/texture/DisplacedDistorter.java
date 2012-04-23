package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;
import processing.core.PVector;

public class DisplacedDistorter implements Distorter {

	public float offsetX;
	public float offsetY;

	public void distort(PVector origCoord, PVector distCoord, int value) {
		if (value == 0) {
			distCoord.x = origCoord.x;
			distCoord.y = origCoord.y;
		} else {
			distCoord.x = origCoord.x + offsetX;
			distCoord.y = origCoord.y + offsetY;
			PApplet.println("x,y: " + distCoord.x + "," + distCoord.y + " - ox,oy:" + offsetX + ", " + offsetY);
		}
	}

}

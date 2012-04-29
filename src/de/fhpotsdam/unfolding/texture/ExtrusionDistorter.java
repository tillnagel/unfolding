package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;
import processing.core.PVector;

public class ExtrusionDistorter implements Distorter {

	public void distort(PVector origCoord, PVector distCoord, int col) {
		distCoord.x = origCoord.x;
		distCoord.y = origCoord.y;

		PApplet p = PAppletFactory.getInstance();

		// Random
		// distCoord.z = (float) (Math.random()) * 10;
		// Wave / Ripple
		// distCoord.z = (float) Math.sin(distCoord.x * 1.1f) * 5f;
		// Perlin Noise
		// distCoord.z = PApplet.sin(p.frameCount / 20f) * p.noise(distCoord.x, distCoord.y) * 50 -
		// 25;

		float brightness = p.brightness(col);
		float z = PApplet.map(brightness, 0, 255, -1, 1);
		// PApplet.println(col + ". " + brightness + "," + z);
		distCoord.z = z * PApplet.sin(p.frameCount / 20f) * 50;
	}

}

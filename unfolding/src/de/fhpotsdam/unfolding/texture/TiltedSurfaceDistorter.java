package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;
import processing.core.PVector;

public class TiltedSurfaceDistorter implements Distorter {

	@Override
	public void distort(PVector origCoord, PVector distCoord) {
		distCoord.x = origCoord.x;
		distCoord.y = origCoord.y;

		PApplet p = PAppletFactory.getInstance();

		// Random
		// distCoord.z = (float) (Math.random()) * 10;
		// Wave / Ripple
		// distCoord.z = (float) Math.sin(distCoord.x * 1.1f) * 5f;
		// Perlin Noise
		distCoord.z = PApplet.sin(p.frameCount / 20f) * p.noise(distCoord.x, distCoord.y) * 50 - 25;
	}

	public void distort(PVector origCoord, PVector distCoord, int col) {
		PApplet.println(col);
		distCoord.x = origCoord.x;
		distCoord.y = origCoord.y;
		PApplet p = PAppletFactory.getInstance();
		float brightness = p.brightness(col);
		float z = PApplet.map(brightness, 0, 255, -25, 25);
		//PApplet.println(col + ". " + brightness + "," + z);
		distCoord.z = PApplet.map(brightness, 0, 255, -25, 25);
		//distCoord.z = PApplet.sin(p.frameCount / 20f) * p.noise(distCoord.x, distCoord.y) * 50 - 25;
	}

}

package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Distorts the z value of the coordinate based on the sinus value of x.
 */
public class RippleDistorter implements Distorter {

	@Override
	public void distort(PVector origCoord, PVector distCoord, int value) {
		distCoord.x = origCoord.x;
		distCoord.y = origCoord.y;

		PApplet p = PAppletFactory.getInstance();

		// Wave / Ripple
		distCoord.z = (float) Math.sin(distCoord.x * 1.1f) * 5f;
	}

}

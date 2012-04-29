package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class AbstractLensDistorter implements Distorter {

	protected static final float DEFAULT_ZOOM = 2;

	private static final float HOTFIX = 0.0001f;

	protected PVector center = new PVector();

	public void setCenter(PVector center) {
		this.center = center;
	}

	public void setCenter(float x, float y) {
		// FIXME Center coordinates must not be exact multiplies of meshStep
		center.set(x, y + HOTFIX, 0);
	}

	@Override
	public void distort(PVector origCoord, PVector distCoord, int value) {
		float origRadius = PVector.dist(center, origCoord);
		float newRadius = interpolateRadius(origRadius);

		PVector orgFromCenter = new PVector(0, 0, 0);
		PVector.sub(origCoord, center, orgFromCenter);
		float n = PApplet.map(newRadius, 0, origRadius, 0, 1);
		orgFromCenter.mult(n);

		PVector.add(orgFromCenter, center, distCoord);
	}

	protected abstract float interpolateRadius(float radius);

}

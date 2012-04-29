package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;

/**
 * Simple distortion with hard edge.
 */
public class LinearInterpolationDistorter extends AbstractLensDistorter {

	protected float r0;
	protected float r1;
	protected float zoom = DEFAULT_ZOOM;
	protected float s1 = 1 / zoom;

	public LinearInterpolationDistorter(float radius0, float radius1) {
		this.r0 = radius0;
		this.r1 = radius1;

		if (radius1 < (radius0 / s1)) {
			PApplet.println("Distortion may look weird. Radius 1 should be greater.");
		}
	}

	protected float interpolateRadius(float radius) {
		float s0 = 1;
		radius = radius * zoom;

		if (radius <= r0) {
			radius = radius;
		}
		if (radius > r0 && radius <= r1) {
			radius = r0 + ((r1 * s1 - r0) / (r1 - r0)) * (radius - r0);
		}
		if (radius > r1) {
			radius = radius * s1;
		}
		return radius;
	}

	public void setRadius(int r) {
		r0 = r;
		r1 = r * zoom;
	}

	public void setOuterRadius(int r) {
		r0 = r;
	}

	public void setInnerRadius(int r) {
		r1 = r;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		this.s1 = 1 / zoom;
	}

}

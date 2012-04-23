package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;

/**
 * Scale interpolation algorithm by [Bogen 2008], Visual Navigation with Schematic Maps
 */
public class ScaleInterpolationDistorter extends AbstractLensDistorter {

	protected float r0;
	protected float r1;
	protected float zoom = DEFAULT_ZOOM;
	protected float s1 = 1 / zoom;

	public ScaleInterpolationDistorter(float radius0, float radius1) {
		this.r0 = radius0;
		this.r1 = radius1;
	}

	protected float interpolateRadius(float radius) {
		float s0 = 1;
		radius = radius * zoom;

		if (radius <= r0) {
			radius = radius;
		}
		if (radius > r0 && radius <= r1) {
			radius = radius + ((PApplet.sq(radius - r0) * (s1 - 1)) / (2 * (r1 - r0)));
		}
		if (radius > r1) {
			radius = r0 + (r1 - r0) * ((1 + s1) / 2) + (radius - r1) * s1;
		}
		return radius;
	}

	public void setRadius(int r) {
		r0 = r;
		r1 = r * zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		this.s1 = 1 / zoom;
	}

}

package de.fhpotsdam.unfolding.geo;

import processing.core.PVector;

public class Transformation {

	public double ax, bx, cx, ay, by, cy;

	public Transformation(double ax, double bx, double cx, double ay, double by, double cy) {
		this.ax = ax;
		this.bx = bx;
		this.cx = cx;
		this.ay = ay;
		this.by = by;
		this.cy = cy;
	}

	public PVector transform(PVector point) {
		return new PVector((float) (ax * point.x + bx * point.y + cx), (float) (ay * point.x + by * point.y + cy));
	}

	public PVector untransform(PVector point) {
		return new PVector((float) ((point.x * by - point.y * bx - cx * by + cy * bx) / (ax * by - ay * bx)),
				(float) ((point.x * ay - point.y * ax - cx * ay + cy * ax) / (bx * ay - by * ax)));
	}

}

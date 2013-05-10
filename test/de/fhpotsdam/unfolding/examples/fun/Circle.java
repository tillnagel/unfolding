package de.fhpotsdam.unfolding.examples.fun;

import processing.core.PApplet;

public class Circle {

	PApplet p;

	float x;
	float y;
	float radius;
	int col;
	boolean moved = false;
	boolean active = false;
	boolean showGlow = true;

	Integrator xItgr;
	Integrator yItgr;
	Integrator radiusItgr;
	ColorIntegrator colorItgr;

	public Circle(PApplet p, float x, float y, float radius) {
		this.p = p;
		xItgr = new Integrator(x);
		yItgr = new Integrator(y);
		radiusItgr = new Integrator(radius);
		colorItgr = new ColorIntegrator(p.color(0));

		this.x = x;
		this.y = y;
		this.radius = radius;
		this.col = p.color(0);
	}

	void draw() {
		this.x = xItgr.value;
		this.y = yItgr.value;
		this.radius = radiusItgr.value;
		float d = radiusItgr.value * 2;

		if (showGlow) {
			int outer = p.color(p.red(colorItgr.value), p.green(colorItgr.value), p.blue(colorItgr.value), 40);
			p.noStroke();
			p.fill(outer);
			p.ellipse(x, y, d * 1.2f, d * 1.2f);
			p.stroke(0);
		}

		if (active) {
			p.stroke(255, 0, 0);
		} else {
			p.stroke(0);
		}
		p.fill(colorItgr.value);
		p.ellipse(x, y, d, d);
	}

	void update() {
		xItgr.update();
		yItgr.update();
		radiusItgr.update();
		colorItgr.update();
	}

	void moveTo(float x, float y, float radius, int col) {
		xItgr.target(x);
		yItgr.target(y);
		radiusItgr.target(radius);
		colorItgr.target(col);
		moved = true;
	}

	boolean isMoved() {
		return moved;
	}

	boolean intersect(Circle otherCircle) {
		return this.radius + otherCircle.radius > PApplet.dist(this.x, this.y, otherCircle.x, otherCircle.y);
	}

	boolean contains(int x, int y) {
		return this.radius > PApplet.dist(x, y, this.x, this.y);
	}
}
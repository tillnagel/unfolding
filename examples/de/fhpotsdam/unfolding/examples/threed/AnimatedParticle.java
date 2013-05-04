package de.fhpotsdam.unfolding.examples.threed;

import processing.core.PApplet;
import processing.core.PVector;

public class AnimatedParticle {

	PApplet p;

	PVector pos1;
	PVector pos2;
	float height;

	float timer = 0;
	float timerDiff = 0;
	
	int color;

	public AnimatedParticle(PApplet p) {
		this.p = p;
		
		timerDiff = p.random(0.01f, 0.04f);
		color = p.color(p.random(255), p.random(255), 255);
	}

	public AnimatedParticle(PApplet p, PVector pos1, PVector pos2) {
		this(p);
		setPositions(pos1, pos2);
	}

	public void update() {
		timer += timerDiff;
		if (timer > 1) {
			timer = 0;
		}
	}

	public void setPositions(PVector pos1, PVector pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.height = pos1.dist(pos2);
	}

	public void draw() {
		PVector v = getBezierPoint3D();
		
		p.stroke(color, 50);
		p.pushMatrix();
		p.translate(v.x, v.y, v.z);
		p.sphere(2);
		p.popMatrix();
	}

	protected PVector getBezierPoint3D() {
		float x = p.bezierPoint(pos1.x, pos1.x, pos2.x, pos2.x, timer);
		float y = p.bezierPoint(pos1.y, pos1.y, pos2.y, pos2.y, timer);
		float z = p.bezierPoint(0, height, height, 0, timer);
		return new PVector(x, y, z);
	}

}

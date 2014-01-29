package de.fhpotsdam.unfolding;

import processing.core.PApplet;

public class TransparentThingsOnOpenGLMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(600, 600, OPENGL);
		map = new UnfoldingMap(this);
	}

	public void draw() {
		background(0);
		map.draw();

		fill(255, 0, 0, 50);
		ellipse(width / 2, height / 2, 200, 200);
	}

	public void test() {
		// PGraphics pg = createGraphics((int) width, (int) height);
		// pg.beginDraw();
		// pg.background(0);
		// pg.endDraw();
		//
		// pushStyle();
		// blendMode(REPLACE);
		// image(pg, 0, 0);
		// popStyle();
		// //blendMode(BLEND);
	}

}

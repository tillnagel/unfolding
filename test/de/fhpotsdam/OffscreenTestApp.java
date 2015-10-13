package de.fhpotsdam;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Test for https://github.com/processing/processing/issues/3858
 */
public class OffscreenTestApp extends PApplet {

	PImage testTile;
	PGraphics pg;
	PGraphics pg2;

	public void settings() {
		size(800, 600, P3D);
	}

	public void setup() {
		testTile = loadImage("http://a.basemaps.cartocdn.com/light_all/1/1/1.png");
		pg = createGraphics((int) width, (int) height, P3D);
		pg.smooth(g.smooth);
		pg2 = createGraphics((int) width, (int) height, P3D);
	}

	public void draw() {
		pg.beginDraw();
		pg.clear();
		pg.image(testTile, 0, 0);
		pg.endDraw();
		
		pushMatrix();
		translate(width/2, height/2);
		image(pg, 0, 0);
		popMatrix();
		
		fill(255, 0, 0);
		ellipse(mouseX, mouseY, 100, 100);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { OffscreenTestApp.class.getName() });
	}
}

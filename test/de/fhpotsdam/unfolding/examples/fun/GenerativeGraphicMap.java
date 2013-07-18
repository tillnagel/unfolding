package de.fhpotsdam.unfolding.examples.fun;

import processing.core.PApplet;
import processing.core.PGraphics;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Some very simple experiment drawing randomly placed circles in color of the map background.
 */
public class GenerativeGraphicMap extends PApplet {

	UnfoldingMap map;
	PGraphics pg;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
		pg = map.mapDisplay.getInnerPG();
		pg.loadPixels();
		
		background(0);
		for (int i = 0; i < 2000; i++) {
			drawPoint();
		}
		// No updatePixels() as points are drawn directly onto the canvas.
	}

	public void drawPoint() {
		float pSize = 2.0f + (mouseX / (float) width) * 16.0f;
		int x = (int) random(width);
		int y = (int) random(height);
		int c = pg.pixels[x + y * width];
		fill(c, 100);
		noStroke();
		ellipse(x, y, pSize * 2, pSize * 2);
		fill(c, 255);
		ellipse(x, y, pSize, pSize);
	}
}

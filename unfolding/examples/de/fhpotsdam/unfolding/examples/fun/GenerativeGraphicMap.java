package de.fhpotsdam.unfolding.examples.fun;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Some very simple experiment drawing randomly placed circles in color of the map background.
 */
public class GenerativeGraphicMap extends PApplet {

	Map map1;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map1 = new Map(this, "map1", 0, 0, 800, 600, true, false, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map1);
	}

	public void draw() {
		background(0);
		map1.draw();
		loadPixels();
		background(0);
		for (int i = 0; i < 2000; i++) {
			drawPoint();
		}
		// No updatePixels() as points are drawn directly onto the canvas.
	}

	public void keyPressed() {
		println("fps:" + frameRate);
	}

	public void drawPoint() {
		float pSize = 2.0f + (mouseX / (float) width) * 16.0f;
		int x = (int) random(width);
		int y = (int) random(height);
		int c = pixels[x + y * width];
		fill(pixels[x + y * width], 100);
		noStroke();
		ellipse(x, y, pSize * 2, pSize * 2);
		fill(pixels[x + y * width], 255);
		ellipse(x, y, pSize, pSize);
	}
}

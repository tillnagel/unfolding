package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

public class GLGraphicsMapDisplay extends ProcessingMapDisplay implements PConstants {

	protected GLGraphicsOffScreen pg;

	protected float opacity = 255;

	public GLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX,
			float offsetY, float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);

		pg = new GLGraphicsOffScreen(papplet, (int) width, (int) height);
	}

	protected PGraphics getPG() {
		pg.beginDraw();
		return pg;
	}

	protected void postDraw() {
		pg.endDraw();

		papplet.pushMatrix();
		papplet.applyMatrix(matrix);
		papplet.image(pg.getTexture(), 0, 0);
		papplet.popMatrix();
	}

}

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

	public PGraphics getPG() {
		return pg;
	}

	protected void postDraw() {
		PGraphics outerPG = getOuterPG();

		outerPG.pushMatrix();
		outerPG.translate(offsetX, offsetY);
		outerPG.applyMatrix(matrix);
		outerPG.image(pg.getTexture(), 0, 0);

		if (markerManager != null) {
			markerManager.drawOuter();
		}

		outerPG.popMatrix();
	}

}

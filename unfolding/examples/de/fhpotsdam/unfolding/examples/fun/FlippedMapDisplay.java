package de.fhpotsdam.unfolding.examples.fun;

import codeanticode.glgraphics.GLTexture;
import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.mapdisplay.GLGraphicsMapDisplay;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

public class FlippedMapDisplay extends GLGraphicsMapDisplay {

	public FlippedMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY, float width,
			float height) {
		super(papplet, provider, offsetX, offsetY, width, height);
	}

	protected void postDraw() {
		PGraphics outerPG = getOuterPG();

		outerPG.pushMatrix();
		outerPG.translate(offsetX, offsetY);
		outerPG.applyMatrix(matrix);
		
		// Flip test
		GLTexture map = pg.getTexture();
		outerPG.scale(-1, 1);
		outerPG.translate(-width, 0);
		outerPG.image(map, 0, 0);
		
		if (markerManager != null) {
			markerManager.drawOuter();
		}

		outerPG.popMatrix();
	}
	
}

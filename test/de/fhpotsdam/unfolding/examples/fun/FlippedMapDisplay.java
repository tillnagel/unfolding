package de.fhpotsdam.unfolding.examples.fun;

import processing.core.PApplet;
import processing.core.PGraphics;


import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

public class FlippedMapDisplay extends OpenGLMapDisplay {

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
		outerPG.scale(-1, 1);
		outerPG.translate(-width, 0);
		outerPG.image(getInnerPG(), 0, 0);

		for (MarkerManager<Marker> mm : markerManagerList) {
			mm.draw();
		}

		outerPG.popMatrix();
	}

}

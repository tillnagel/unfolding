package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

public class GLGraphicsMapDisplay extends ProcessingMapDisplay implements PConstants {

	// Inner map (and inner marker) will be drawn on this.
	protected PGraphics offscreenPG;
	
	// Outer marker will be drawn on this
	protected PGraphics offscreenCutoffPG;

	protected float opacity = 255;

	public GLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);

//		offscreenPG = new GLGraphicsOffScreen(papplet, (int) width, (int) height, true);
		offscreenCutoffPG = papplet.createGraphics((int) width, (int) height, OPENGL);
		offscreenPG = papplet.createGraphics((int) width, (int) height, OPENGL);
	}

	@Override
	public void resize(float width, float height) {
		super.resize(width, height);
		offscreenCutoffPG = papplet.createGraphics((int) width, (int) height, OPENGL);
		offscreenPG = papplet.createGraphics((int) width, (int) height, OPENGL);
	}

	@Override
	public PGraphics getInnerPG() {
		return offscreenPG;
	}

	@Override
	public PGraphics getOuterPG() {
		return offscreenCutoffPG;
	}

	@Override
	protected void postDraw() {
		// Draws inner map (with inner marker) and outer marker
		offscreenCutoffPG.beginDraw();
		offscreenCutoffPG.image(offscreenPG, 0, 0);
		for (MarkerManager<Marker> mm : markerManagerList) {
			mm.draw();
		}
		offscreenCutoffPG.endDraw();

		// Transforms (outer) map pane, and draws inner map + marker onto canvas
		// This cuts off marker at the border.
		PGraphics canvasPG = papplet.g;
		canvasPG.pushMatrix();
		canvasPG.translate(offsetX, offsetY);
		canvasPG.applyMatrix(matrix);
		canvasPG.image(offscreenCutoffPG, 0, 0);
		canvasPG.popMatrix();
	}

}

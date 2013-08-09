package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

// TODO: Unify with GLGRaphics and create as flag/property
public class MaskedOpenGLMapDisplay extends OpenGLMapDisplay implements PConstants {

	protected MaskedMapDisplayShader maskShader;
//	private GLTexture maskedTex;

	public MaskedOpenGLMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);
		
		maskShader = new MaskedMapDisplayShader(papplet,width,height);
	}

	@Override
	public void resize(float width, float height) {
		super.resize(width, height);
		maskShader.resize(width, height);
	}

	public PGraphics getMask() {
		return maskShader.getMask();
	}

	protected void postDraw() {
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
		canvasPG.shader(maskShader.getShader());
		canvasPG.image(offscreenCutoffPG, 0, 0);
		canvasPG.popMatrix();
	}

}

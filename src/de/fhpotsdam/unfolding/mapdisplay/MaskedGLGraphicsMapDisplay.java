package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.PShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

// TODO: Unify with GLGRaphics and create as flag/property
public class MaskedGLGraphicsMapDisplay extends GLGraphicsMapDisplay implements PConstants {

	protected PGraphics maskImage;
	protected PShader maskShader;
//	private GLTexture maskedTex;

	public MaskedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);
		
		maskImage = papplet.createGraphics((int)width, (int)height, OPENGL);
		maskImage.noSmooth();
		
		maskShader = papplet.loadShader("test/mask.glsl");
		maskShader.set("mask", maskImage);
	}

	@Override
	public void resize(float width, float height) {
		super.resize(width, height);
		maskImage.resize((int)width, (int)height);
	}

	public PGraphics getMask() {
		return maskImage;
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
//		canvasPG.translate(offsetX, offsetY);
//		canvasPG.applyMatrix(matrix);
		canvasPG.shader(maskShader);
		canvasPG.image(offscreenCutoffPG, 0, 0);
		canvasPG.popMatrix();
	}

}

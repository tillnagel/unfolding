package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.texture.Distorter;
import de.fhpotsdam.unfolding.texture.LinearInterpolationDistorter;
import de.fhpotsdam.unfolding.texture.TextureDistorter;

public class DistortedGLGraphicsMapDisplay extends OpenGLMapDisplay {

	public Distorter distorter;
	public TextureDistorter textureDistorter;

	public DistortedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, null, offsetX, offsetY, width, height);

		distorter = new LinearInterpolationDistorter(width / 2, height / 2);
		textureDistorter = new TextureDistorter(papplet, width, height, 10);
		textureDistorter.setDistorter(distorter);
	}

	public DistortedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height, Distorter distorter) {
		super(papplet, provider, null, offsetX, offsetY, width, height);

		this.distorter = distorter;
		textureDistorter = new TextureDistorter(papplet, width, height, 10);
		textureDistorter.setDistorter(distorter);
	}

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
		
		textureDistorter.draw(canvasPG, offscreenPG);
		//canvasPG.image(offscreenCutoffPG.getTexture(), 0, 0);
		canvasPG.popMatrix();
	}


}

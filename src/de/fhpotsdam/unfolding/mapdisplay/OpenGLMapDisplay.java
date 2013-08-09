package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

public class OpenGLMapDisplay extends P2DMapDisplay implements PConstants {

	// Inner map (and inner marker) will be drawn on this.
	protected PGraphics offscreenPG;
	// Outer marker will be drawn on this
	protected PGraphics offscreenCutoffPG;

	protected float opacity = 255;
	
	protected MaskedMapDisplayShader mapDisplayShader = null; 

	public OpenGLMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);

		offscreenPG = papplet.createGraphics((int) width, (int) height,OPENGL);
		offscreenCutoffPG = papplet.createGraphics((int) width, (int) height,OPENGL);
	}
	
	public void setMapDisplayShader(MaskedMapDisplayShader shader){
		this.mapDisplayShader = shader;
	}
	
	public MaskedMapDisplayShader getMapDisplayShader(){
		return mapDisplayShader;
	}

	@Override
	public void resize(float width, float height) {
		super.resize(width, height);
		offscreenPG = papplet.createGraphics((int) width, (int) height,OPENGL);
		offscreenCutoffPG = papplet.createGraphics((int) width, (int) height,OPENGL);
		if(mapDisplayShader != null){
			mapDisplayShader.resize(width, height);
		}
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
		if(mapDisplayShader != null){
			canvasPG.shader(mapDisplayShader.getShader());
		}
		canvasPG.image(offscreenCutoffPG, 0, 0);
		canvasPG.popMatrix();
	}
}

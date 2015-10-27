package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

@SuppressWarnings("rawtypes")
public class OpenGLMapDisplay extends Java2DMapDisplay implements PConstants {

	protected String renderer;
	// Inner map (and inner marker) will be drawn on this.
	protected PGraphics offscreenPG;
	// Outer marker will be drawn on this
	protected PGraphics offscreenCutoffPG;

	protected float opacity = 255;

	protected MapDisplayShader mapDisplayShader = null;

	public OpenGLMapDisplay(PApplet papplet, AbstractMapProvider provider, String renderer, float offsetX,
			float offsetY, float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);

		if (renderer == null || renderer.equals("")) {
			try {
				Class P2DClass = Class.forName(P2D);
				Class P3DClass = Class.forName(P3D);
				if (P2DClass.isInstance(papplet.g)) {
					this.renderer = P2D;
				} else if (P3DClass.isInstance(papplet.g)) {
					this.renderer = P3D;
				} else {
					// Can be reached when a renderer is used which extends PGraphicsOpenGL but not PGraphics3D
					// Thanks to @codeanticode (Andres Colubri) for suggestion.
					if (papplet.g.is2D()) {
						this.renderer = P2D;
					} else {
						this.renderer = OPENGL;
					}
				}
			} catch (ClassNotFoundException e) {
				this.renderer = OPENGL;
			}
		} else {
			this.renderer = renderer;
		}

		offscreenPG = papplet.createGraphics((int) width, (int) height, this.renderer);
		offscreenPG.smooth(papplet.g.smooth);
		offscreenCutoffPG = papplet.createGraphics((int) width, (int) height, this.renderer);
		offscreenCutoffPG.smooth(papplet.g.smooth);
	}

	public void setMapDisplayShader(MapDisplayShader shader) {
		this.mapDisplayShader = shader;
	}

	public MapDisplayShader getMapDisplayShader() {
		return mapDisplayShader;
	}

	public String getRenderer() {
		return renderer;
	}

	@Override
	public void resize(float width, float height) {
		super.resize(width, height);

		if (offscreenPG != null) {
			offscreenPG.dispose();
		}
		if (offscreenCutoffPG != null) {
			offscreenCutoffPG.dispose();
		}

		offscreenPG = papplet.createGraphics((int) width, (int) height, renderer);
		offscreenPG.smooth(papplet.g.smooth);
		offscreenCutoffPG = papplet.createGraphics((int) width, (int) height, renderer);
		offscreenCutoffPG.smooth(papplet.g.smooth);

		if (mapDisplayShader != null) {
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
		offscreenCutoffPG.clear();
		if (mapDisplayShader != null) {
			// NB: Uses offscreenPG (and not offscreenCutofPG) to not get 'Shader must be COLOR type' error
			mapDisplayShader.shadeWithoutMarkers(offscreenPG);
		}
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
		if (canvasPG.is3D()) {
			canvasPG.applyMatrix(matrix);
		} else {
			canvasPG.applyMatrix(matrix.m00, matrix.m01, matrix.m03, matrix.m10, matrix.m11, matrix.m13);
		}
		if (mapDisplayShader != null) {
			mapDisplayShader.shadeWithMarkers(canvasPG);
		}
		canvasPG.pushStyle();
		canvasPG.image(offscreenCutoffPG, 0, 0);
		canvasPG.popStyle();
		canvasPG.popMatrix();
		// canvasPG.resetShader();
	}
}

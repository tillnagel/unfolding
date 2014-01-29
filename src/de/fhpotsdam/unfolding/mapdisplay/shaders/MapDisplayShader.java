package de.fhpotsdam.unfolding.mapdisplay.shaders;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;

public class MapDisplayShader {

	protected PApplet p;

	protected PShader shader;

	public MapDisplayShader(PApplet p) {
		this.p = p;
	}
	
	public PShader getShader() {
		return shader;
	}

	/**
	 * Resizes the shader. Optional, only implement if needed.
	 * 
	 * @param width
	 *            The new width of the canvas.
	 * @param height
	 *            The new height of the canvas.
	 */
	public void resize(float width, float height) {

	}

	/**
	 * Applies the shader to the map display canvas.
	 * 
	 * @param innerCanvas
	 *            The inner canvas. 
	 */
	public void shadeWithoutMarkers(PGraphics innerCanvas) {

	}

	public void shadeWithMarkers(PGraphics innerCanvas) {

	}

}

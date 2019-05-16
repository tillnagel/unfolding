package de.fhpotsdam.unfolding.examples.mask.custom;

import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * This shader deforms a map.
 */
public class DeformMapDisplayShader extends MapDisplayShader {

	float posX;
	float posY;
	
	public DeformMapDisplayShader(PApplet p, float width, float height) {
		super(p);
		shader = p.loadShader("shader/deform.glsl");
		shader.set("resolution", width, height);
	}

	@Override
	public void shadeWithMarkers(PGraphics mapDisplayCanvas) {
		shader.set("time", (float) (p.millis() / 1000.0));
		shader.set("mouse", posX, posY);
		mapDisplayCanvas.textureWrap(PApplet.REPEAT);
		mapDisplayCanvas.shader(shader);
	}
	
	public void setPos(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}

}

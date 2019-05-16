package de.fhpotsdam.unfolding.mapdisplay.shaders;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * This shader blurs a map.
 * 
 * Implements shade without markers, i.e. blurs only the map but not the markers.
 * Uses two-pass, i.e. calls PGraphics.shader(blurShader) twice.
 * 
 */
public class BlurredMapDisplayShader extends MapDisplayShader {

	private static final int DEFAULT_BLUR_SIZE = 9;
	private static final float DEFAULT_SIGMA = 5.0f;

	public BlurredMapDisplayShader(PApplet p, int blurSize, float sigma) {
		super(p);
		shader = p.loadShader("shader/blur.glsl");
		shader.set("blurSize", blurSize);
		shader.set("sigma", sigma);
	}

	public BlurredMapDisplayShader(PApplet p) {
		this(p, DEFAULT_BLUR_SIZE, DEFAULT_SIGMA);
	}

	@Override
	public void shadeWithoutMarkers(PGraphics mapDisplayCanvas) {
		// Two-pass Gaussian blur, first vertical, then horizontal
		
		shader.set("horizontalPass", 0);
		mapDisplayCanvas.shader(shader);
		shader.set("horizontalPass", 1);
		mapDisplayCanvas.shader(shader);
	}

}

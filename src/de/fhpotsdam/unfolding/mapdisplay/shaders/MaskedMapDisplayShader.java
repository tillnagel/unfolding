package de.fhpotsdam.unfolding.mapdisplay.shaders;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * This shader masks a map. The offscreen graphic canvas as mask
 * 
 * 
 */
public class MaskedMapDisplayShader extends MapDisplayShader {

	/** The mask canvas. */
	protected PGraphics mask;

	/**
	 * Creates a MaskedMapDisplayShader to draw the mask dynamically. Call {@link #getMask()} to get the mask canvas to
	 * draw on.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param width
	 *            The width of the mask.
	 * @param height
	 *            The height of the mask.
	 */
	public MaskedMapDisplayShader(PApplet p, float width, float height) {
		super(p);
		mask = p.createGraphics((int) width, (int) height, PApplet.P2D);
		mask.noSmooth();

		shader = p.loadShader("shader/mask.glsl");
		shader.set("mask", mask);
	}

	/**
	 * Creates a MaskedMapDisplayShader with the given mask image.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param width
	 *            The width of the mask.
	 * @param height
	 *            The height of the mask.
	 * @param maskImage
	 *            An image to be used as mask.
	 */
	public MaskedMapDisplayShader(PApplet p, float width, float height, PImage maskImage) {
		this(p, width, height);
		mask.beginDraw();
		mask.image(maskImage, 0, 0);
		mask.endDraw();
	}

	public void resize(float width, float height) {
		mask.resize((int) width, (int) height);
	}

	/**
	 * Returns the mask canvas to dynamically create or update the mask.
	 * 
	 * @return The mask.
	 */
	public PGraphics getMask() {
		return mask;
	}

	@Override
	public void shadeWithMarkers(PGraphics mapDisplayCanvas) {
		mapDisplayCanvas.shader(getShader());
	}

}

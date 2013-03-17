package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import codeanticode.glgraphics.GLTexture;
import codeanticode.glgraphics.GLTextureFilter;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

// TODO: Unify with GLGRaphics and create as flag/property
public class MaskedGLGraphicsMapDisplay extends GLGraphicsMapDisplay implements PConstants {

	public GLGraphicsOffScreen mask;
	private GLTextureFilter maskFilter;
	private GLTexture maskedTex;

	public MaskedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);
		
		mask = new GLGraphicsOffScreen(papplet, (int) width, (int) height, true);
		mask.smooth();
		maskedTex = new GLTexture(papplet, (int) width, (int) height);
		maskFilter = new GLTextureFilter(papplet, "Mask.xml");
	}

	@Override
	public void resize(float width, float height) {
		super.resize(width, height);
		mask = new GLGraphicsOffScreen(papplet, (int) width, (int) height);
	}

	public GLGraphicsOffScreen getMask() {
		return mask;
	}

	protected void postDraw() {
		PGraphics outerPG = getOuterPG();

		outerPG.pushMatrix();
		outerPG.translate(offsetX, offsetY);
		outerPG.applyMatrix(matrix);

		// maskFilter.setParameterValue("mask_factor", 0.0f);
		maskFilter.apply(new GLTexture[] { offscreenPG.getTexture(), mask.getTexture() }, maskedTex);

		outerPG.image(maskedTex, 0, 0);

		for (MarkerManager<? extends Marker> mm : this) {
			mm.draw();
		}

		outerPG.popMatrix();
	}

}

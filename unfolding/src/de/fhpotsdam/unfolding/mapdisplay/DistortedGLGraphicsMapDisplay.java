package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.texture.Distorter;
import de.fhpotsdam.unfolding.texture.LinearInterpolationDistorter;
import de.fhpotsdam.unfolding.texture.TextureDistorter;

public class DistortedGLGraphicsMapDisplay extends GLGraphicsMapDisplay {

	public Distorter distorter;
	TextureDistorter textureDistorter;

	public DistortedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);

		distorter = new LinearInterpolationDistorter(width / 2, height / 2);
		textureDistorter = new TextureDistorter(papplet, width, height, 10);
		textureDistorter.setDistorter(distorter);
	}

	public DistortedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY,
			float width, float height, Distorter distorter) {
		super(papplet, provider, offsetX, offsetY, width, height);

		this.distorter = distorter;
		textureDistorter = new TextureDistorter(papplet, width, height, 10);
		textureDistorter.setDistorter(distorter);
	}

	protected void postDraw() {
		PGraphics outerPG = getOuterPG();
		
		outerPG.pushMatrix();
		outerPG.translate(offsetX, offsetY);
		// REVISIT outer matrix not applied (instead of as in GLGraphicsMapDisplay)
		
		textureDistorter.draw(outerPG, pg.getTexture());
		
		outerPG.popMatrix();
	}
	
	protected void postDraw2() {
		PGraphics p = papplet.g;
		p.pushMatrix();
		p.translate(offsetX, offsetY);
		textureDistorter.draw(p, pg.getTexture());
		p.popMatrix();
	}

}

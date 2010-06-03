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
	
	public DistortedGLGraphicsMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX,
			float offsetY, float width, float height) {
		super(papplet, provider, offsetX, offsetY, width, height);
		
		distorter = new LinearInterpolationDistorter(70, 200);
		textureDistorter = new TextureDistorter(papplet, 800, 600, 10);
		textureDistorter.setDistorter(distorter);
	}

	protected void postDraw() {
		pg.endDraw();
		
		PGraphics p = papplet.g;
		p.pushMatrix();
		p.translate(offsetX, offsetY);
		
		textureDistorter.draw(p, pg.getTexture());
		
		p.popMatrix();
	}
	
	public void setCenter(float x, float y) {
		distorter.setCenter(x, y);
	}

}

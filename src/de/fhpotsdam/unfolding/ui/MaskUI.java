package de.fhpotsdam.unfolding.ui;

import javax.media.opengl.GL;

import codeanticode.glgraphics.GLTexture;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

public class MaskUI {
	
	private PApplet p;
	private AbstractMapDisplay mapDisplay;
	private GL gl;
	private PGraphicsOpenGL pgl;
	
	public GLTexture maskTex;

	public MaskUI(PApplet p) {
		this.p = p;
		this.mapDisplay = mapDisplay;
		
		maskTex = new GLTexture(p, p.width, p.height);

		this.pgl = (PGraphicsOpenGL) p.g; 
		this.gl = pgl.gl;
	}

	
	public void draw() {
		gl.glBlendFunc(gl.GL_DST_COLOR, gl.GL_ZERO);
		p.image(maskTex, 0, 0, p.width, p.height);
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void setTexture(GLTexture tex){
		maskTex=tex;
	}

}

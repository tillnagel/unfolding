package de.fhpotsdam.unfolding.ui;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import codeanticode.glgraphics.GLTexture;

public class MaskUI {
	
	private PApplet p;
	private GL gl;
	private PGraphicsOpenGL pgl;
	
	public GLGraphicsOffScreen c;
	public GLTexture maskTex;

	public MaskUI(PApplet p) {
		this.p = p;
		this.c = new GLGraphicsOffScreen(p, p.width, p.height);
		this.maskTex = new GLTexture(p, p.width, p.height);

		this.pgl = (PGraphicsOpenGL) p.g; 
		this.gl = pgl.gl;
	}

	
	public void draw() {
		maskTex = c.getTexture();
		gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
		p.image(maskTex, 0, 0, p.width, p.height);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void setTexture(GLTexture tex){
		maskTex=tex;
	}

}

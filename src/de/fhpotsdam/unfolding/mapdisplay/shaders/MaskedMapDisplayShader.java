package de.fhpotsdam.unfolding.mapdisplay.shaders;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;

public class MaskedMapDisplayShader{
	
	protected PGraphics maskImage;
	protected PShader shader;
	
	public MaskedMapDisplayShader(PApplet p, float width,float height){
		
		maskImage = p.createGraphics((int)width, (int)height,PApplet.OPENGL);
		maskImage.noSmooth();
		
		shader = p.loadShader("test/mask.glsl");
		shader.set("mask", maskImage);
	}
	
	public void resize(float width,float height){
		maskImage.resize((int)width, (int)height);
	}
	
	public PGraphics getMask() {
		return maskImage;
	}
	
	public PShader getShader(){
		return shader;
	}
}

»package de.fhpotsdam.unfolding.masks;

import jogamp.graph.font.typecast.ot.table.Script;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;

/**
 * App to test MaskShader. Move your mouse to try it out!
 */

public class MaskedShaderApp extends PApplet {

	PShader maskShader;
	PImage srcImage;
	PGraphics maskImage;

	public void setup() {
		size(256, 256, P2D);

		srcImage = loadImage("test/beach.jpg");
		
		maskImage = createGraphics(srcImage.width,srcImage.height,P2D);
		maskImage.noSmooth();
		
		maskShader = loadShader("test/mask.glsl");
		maskShader.set("mask", maskImage);
		println("mouse your mouse over the sketch!");
	}

	public void draw() {
		  maskImage.beginDraw();
		  maskImage.background(0);
		  if (mouseX != 0 && mouseY != 0) {  
		    maskImage.noStroke();
		    maskImage.fill(255, 0, 0);
		    maskImage.ellipse(mouseX, mouseY, 50, 50);
		  }
		  maskImage.endDraw();
		
		  shader(maskShader);    
		  image(srcImage, 0, 0, width, height);
	}

}

package de.fhpotsdam.unfolding.masks;

import processing.core.PApplet;
import processing.core.PGraphics;





public class MaskedGLGraphicsApp extends PApplet {

	GLTexture maskedTex;
	
	GLGraphicsOffScreen buf;

	public void setup() {
		size(256, 256, OPENGL);

		GLTexture imgTex = new GLTexture(this, "test/beach.jpg");
		// GLTexture imgMask = new GLTexture(this, "test/mask.png");
		GLTexture imgMask = new GLTexture(this, 256, 256);

		PGraphics pg = createGraphics(256, 256, P2D);
		pg.beginDraw();
		pg.fill(255, 200);
		pg.ellipse(100, 100, 100, 100);
		pg.endDraw();
		

		imgMask.loadPixels();
		imgMask.copy(pg, 0, 0, 256, 256, 0, 0, 256, 256);
//		int k = 0;
//		for (int j = 0; j < 256; j++)
//			for (int i = 0; i < 256; i++) {
//				if (j < 100)
//					imgMask.pixels[k] = color(255, 30);
//				else
//					imgMask.pixels[k] = color(255, 100);
//				k++;
//			}
		imgMask.loadTexture();

		image(imgMask, 0, 0);

		
		maskedTex = new GLTexture(this, 256, 256);

		GLTextureFilter maskFilter = new GLTextureFilter(this, "Mask.xml");

		maskFilter.setParameterValue("mask_factor", 0.0f);
		maskFilter.apply(new GLTexture[] { imgTex, imgMask }, maskedTex);
		
		image(maskedTex, 0, 0);
	}

	public void draw() {
		//background(20, 255, 30);
		//image(maskedTex, 0, 0);
	}

}

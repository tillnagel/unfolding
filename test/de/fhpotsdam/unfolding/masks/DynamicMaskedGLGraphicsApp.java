package de.fhpotsdam.unfolding.masks;

import processing.core.PApplet;
import processing.core.PGraphics;





public class DynamicMaskedGLGraphicsApp extends PApplet {

	GLTexture maskedTex;

	GLGraphicsOffScreen buf;

	public void setup() {
		size(768, 256, OPENGL);

		GLTexture imgTex = new GLTexture(this, "test/beach.jpg");
		// GLTexture imgMask = new GLTexture(this, "test/mask.png");
		GLTexture imgMask = new GLTexture(this, 256, 256);

		PGraphics pg = createGraphics(256, 256, P2D);
		pg.beginDraw();
		pg.noStroke();
		pg.background(0);
		pg.fill(255, 100);
		pg.ellipse(100, 100, 100, 100);
		pg.fill(0, 100);
		pg.ellipse(200, 150, 100, 100);
		pg.endDraw();

		buf = new GLGraphicsOffScreen(this, width, height, true, 4);
		buf.beginDraw();
		buf.noStroke();
		buf.background(0);
		buf.fill(255, 100);
		buf.ellipse(100, 100, 100, 100);
		buf.fill(0, 100);
		buf.ellipse(200, 150, 100, 100);
		buf.endDraw();

		imgMask.loadPixels();
		// imgMask.updateTexture();
		//buf.loadPixels();
		// imgMask.copy(buf.getTexture(), 0, 0, 256, 256, 0, 0, 256, 256);
		int k = 0;
		for (int j = 0; j < 256; j++)
			for (int i = 0; i < 256; i++) {
				imgMask.pixels[k] = pg.pixels[k];
				// if (j < 100)
				// imgMask.pixels[k] = color(255, 50);
				// else
				// imgMask.pixels[k] = color(255, 100);
				k++;
			}
		// imgMask.loadTexture();
		imgMask.updatePixels();

		//image(buf.getTexture(), 0, 0);
		image(pg, 0, 0);
		image(imgMask, 256, 0);

		maskedTex = new GLTexture(this, 256, 256);

		GLTextureFilter maskFilter = new GLTextureFilter(this, "test/Mask.xml");

		maskFilter.setParameterValue("mask_factor", 0.0f);
		maskFilter.apply(new GLTexture[] { imgTex, imgMask }, maskedTex);

		image(maskedTex, 512, 0);
	}

	public void draw() {
		// background(20, 255, 30);
		// image(maskedTex, 0, 0);

		// image(buf.getTexture(), 0, 0, width, height);
	}

}

package de.fhpotsdam.unfolding.masks;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class MaskedImageApp extends PApplet {

	public static Logger log = Logger.getLogger(MaskedImageApp.class);

	PImage img;
	PGraphics mask;

	float tx, ty;
	float vx, vy = random(1, 2);

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {

		img = loadImage("shader/test800x600.png");

		mask = createGraphics(800, 600, P2D);
		mask.beginDraw();
		mask.background(0);
		mask.endDraw();

		tx = width / 2;
		ty = height / 2;
		vx = random(-2, 2);
		vy = random(-2, 2);
	}

	public void draw() {
		background(0);

		mask.beginDraw();
		mask.smooth();
		mask.background(0);
		mask.fill(255, 30);
		mask.rect(0, 0, width, height);
		animateCircles();
		drawHole(mouseX, mouseY, 120);
		mask.endDraw();

		img.mask(mask);
		image(img, 0, 0);
	}

	public void animateCircles() {
		tx += vx;
		ty += vy;
		if (tx > width || tx < 0) {
			vx = -vx;
		}
		if (ty > height || ty < 0) {
			vy = -vy;
		}
		drawHole(tx, ty, 50);
	}

	public void drawHole(float x, float y, float size) {
		mask.noStroke();
		mask.fill(255, 50);
		mask.ellipse(x, y, size, size);
		mask.fill(255);
		mask.ellipse(x, y, size - 20, size - 20);
	}

	public void keyPressed() {
		println(frameRate);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MaskedImageApp.class.getName() });
	}

}

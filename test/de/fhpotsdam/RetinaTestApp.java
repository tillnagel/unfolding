package de.fhpotsdam;

import processing.core.PApplet;

public class RetinaTestApp extends PApplet {

	public void setup() {
		// default: Java2D, low fps, low quality
		//size(1920, 1080);
		//size(1440, 900);
		
		// Retina2D, high fps, high quality
		size(1920, 1080, "processing.core.PGraphicsRetina2D");
		 
		// P2D (OpenGL), high fps, lowest quality
		//size(1920, 1080, P2D);

		// OpenGL, same as P2D
		//size(1920, 1080, OPENGL);

		smooth();
		frameRate(60);
	}

	public void draw() {
		background(40);
		noStroke();
		ellipse(random(width), random(height), 30, 30);
		ellipse(mouseX, mouseY, 30, 30);
		
		fill(255);
		text("fps: " + frameRate, 10, 10);
		
		println(width + ", "+ height);
	}

}

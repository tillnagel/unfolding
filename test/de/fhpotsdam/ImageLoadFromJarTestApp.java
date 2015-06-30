package de.fhpotsdam;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoadFromJarTestApp extends PApplet {

	PImage img;

	public void setup() {
		size(800, 600);
		
		img = loadImage("ui/unfolding-mini-icon.png");
	}

	public void draw() {
		background(240);
		
		image(img, mouseX, mouseY);
	}

}

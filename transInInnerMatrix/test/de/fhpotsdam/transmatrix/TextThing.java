package de.fhpotsdam.transmatrix;

import processing.core.PApplet;

public class TextThing extends TuioTransformableObject {

	public TextThing(PApplet p, float offsetX, float offsetY, float width, float height) {
		super(p, offsetX, offsetY, width, height);
	}
	
	public void internalDraw() {
		p.stroke(0, 20);
		p.noFill();
		p.rect(0, 0, width, height);
		
		p.fill(0);
		p.textSize(70);
		p.text("Kohlrabisuppe", 0, 60);
	}

}

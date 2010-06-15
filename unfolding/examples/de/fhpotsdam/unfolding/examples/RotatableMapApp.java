package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import processing.core.PVector;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class RotatableMapApp extends PApplet {

	Map map;

	PVector rotateCenter = new PVector(350, 250);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1", 0, 0, 700, 500);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(loadFont("Miso-Light-12.vlw"), 20);
	}

	public void draw() {
		background(0);

		if (mousePressed) {
			//map.rotate(0.01f, rotateCenter);
		}

		map.draw();
	}

	public void mouseClicked() {
		rotateCenter = new PVector(mouseX, mouseY);
	}

	public void keyPressed() {
		rotateCenter = new PVector(mouseX, mouseY);
		
		if (key == 'r') {
			map.rotate(-PI/8, rotateCenter);
		} else if (key == 'l') {
			map.rotate(PI/8, rotateCenter);
		}
	}

}

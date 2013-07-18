package de.fhpotsdam.matrix;

import processing.core.PApplet;


public class GridTest extends PApplet {

	Grid grid;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		grid = new Grid(this, 50, 50, 300, 200);
	}

	public void draw() {
		background(240);

		if (mousePressed) {
			if (mouseButton == LEFT) {
				grid.angle += radians(45);
			} else {
				grid.angle -= 0.01f;
			}
		}
		if (keyPressed) {
			if (key == '+') {
				grid.scale += 0.01f;
			} else if (key == '-') {
				grid.scale -= 0.01f;
			}
		}

		grid.draw();
	}

	public void mouseMoved() {
		grid.centerX = mouseX;
		grid.centerY = mouseY;
	}

	public void mouseDragged() {
		mouseMoved();
	}

	public void keyPressed() {
		if (key == ' ') {

			grid.setCenter(50, 50);
			grid.rotate(radians(10));

			grid.centerX = 350;
			grid.rotate(radians(-10));

			grid.centerX = 50;
			grid.rotate(radians(10));

			grid.centerX = 350;
			grid.rotate(radians(-10));

			grid.centerX = 50;
			grid.rotate(radians(10));

			grid.centerX = 350;
			grid.rotate(radians(-10));
		}
	}
}

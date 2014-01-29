package de.fhpotsdam.matrix;

import processing.core.PApplet;
import processing.core.PMatrix3D;


public class RotationTest extends PApplet {

	float offsetX;
	float offsetY;
	
	float rotateX;
	float rotateY;
	
	float angle;
	float zoom;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();
		
		offsetX = 50;
		offsetY = 50;
	}

	public void draw() {
		background(240);

		if (mousePressed) {
			if (mouseButton == LEFT) {
				angle += 0.01f;
			} else {
				angle -= 0.01f;
			}
			rotateX = mouseX;
			rotateY = mouseY;
		}
		if (keyPressed) {
			if (key == '+') {
				zoom += 0.01f;
			}
			else if (key == '-') {
				zoom -= 0.01f;
			}
			
		}

		pushMatrix();
		translate(offsetX, offsetY);
		drawObject(300, 200);
		popMatrix();
		
		
		// works: inverse matrix
		doMatrix(rotateX, rotateY);
		applyMatrix(matrix);
		translate(offsetX, offsetY);
		drawObject(300, 200);
	}
	
	PMatrix3D matrix = new PMatrix3D();
	
	public void doMatrix(float x, float y) {
		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();
		float oldX = invMatrix.multX(x, y);
		float oldY = invMatrix.multY(x, y);
		
		fill(255, 0, 0, 100);
		ellipse(oldX, oldY, 15, 15);

		matrix = new PMatrix3D();
		matrix.translate(x, y);
		matrix.rotate(angle);
		matrix.translate(-oldX, -oldY);
	}

	public void drawObject(float w, float h) {
		fill(255, 150);
		stroke(0, 150);

		for (float i = 0; i <= w; i += 10) {
			for (float j = 0; j <= h; j += 10) {
				rect(i, j, 10, 10);
			}
		}
	}

}

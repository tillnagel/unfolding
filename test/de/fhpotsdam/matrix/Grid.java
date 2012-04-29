package de.fhpotsdam.matrix;

import processing.core.PApplet;
import processing.core.PMatrix3D;

/**
 * Simple drawing object. Used instead of a map to solely check transformations.
 * 
 */
public class Grid {

	PApplet p;

	float offsetX;
	float offsetY;

	float width;
	float height;

	float centerX;
	float centerY;

	float angle = 0;
	float scale = 1;

	PMatrix3D matrix = new PMatrix3D();

	public Grid(PApplet p, float offsetX, float offsetY, float width, float height) {
		this.p = p;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;

		this.centerX = width / 2;
		this.centerY = height / 2;
	}

	public void draw() {
		p.pushMatrix();
		p.applyMatrix(matrix);
		p.translate(offsetX, offsetY);

		p.fill(255, 150);
		p.stroke(0, 150);

		for (float i = 0; i <= width; i += 10) {
			for (float j = 0; j <= height; j += 10) {
				p.rect(i, j, 10, 10);
			}
		}

		p.popMatrix();
	}

	public void rotate(float angle) {
		this.angle += angle;
		calculateMatrix();
	}

	public void scale(float scale) {
		this.scale *= scale;
		calculateMatrix();
	}

	public void setCenter(float cx, float cy) {
		this.centerX = cx;
		this.centerY = cy;
	}

	public void calculateMatrix() {
		calculateMatrix(centerX, centerY);
	}

	protected void calculateMatrix(float x, float y) {
		// Calculates original position by inverting the current matrix.
		// As the matrix incorporates that position, it stores every transformation, even though
		// the matrix is created anew.

		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();
		float origX = invMatrix.multX(x, y);
		float origY = invMatrix.multY(x, y);

		matrix = new PMatrix3D();
		matrix.translate(x, y);
		matrix.scale(scale);
		matrix.rotate(angle);
		matrix.translate(-origX, -origY);
	}

}

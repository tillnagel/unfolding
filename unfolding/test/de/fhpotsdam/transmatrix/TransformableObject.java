package de.fhpotsdam.transmatrix;

import processing.core.PApplet;
import processing.core.PMatrix3D;

/**
 * Simple drawing object. Used instead of a map to solely check transformations.
 * 
 */
public class TransformableObject {

	PApplet p;

	float offsetX;
	float offsetY;

	float width;
	float height;

	float centerX;
	float centerY;

	float angle = 0;
	float scale = 1;

	int color;

	PMatrix3D matrix = new PMatrix3D();

	public TransformableObject(PApplet p, float offsetX, float offsetY, float width, float height) {
		this.p = p;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;

		this.centerX = width / 2;
		this.centerY = height / 2;

		this.color = p.color(255, 150);
	}

	public void draw() {
		p.pushMatrix();
		p.applyMatrix(matrix);
		p.translate(offsetX, offsetY);

		internalDraw();

		p.popMatrix();
	}

	public void internalDraw() {
		p.fill(color);
		p.stroke(0, 150);

		for (float i = 0; i <= width; i += 10) {
			for (float j = 0; j <= height; j += 10) {
				p.rect(i, j, 10, 10);
			}
		}
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isHit(int checkX, int checkY) {
		float[] check = getTransformedPosition(checkX, checkY, true);
		return (check[0] > offsetX && check[0] < offsetX + width && check[1] > offsetY && check[1] < offsetY
				+ height);
	}

	public float[] getTransformedPosition(float x, float y, boolean inverse) {
		float[] preXY = new float[3];
		PMatrix3D m = new PMatrix3D();
		m.apply(matrix);
		if (inverse) {
			m.invert();
		}
		m.mult(new float[] { x, y, 0 }, preXY);
		return preXY;
	}

	public void rotate(float angle) {
		this.angle += angle;
		calculateMatrix();
	}

	public void scale(float scale) {
		this.scale *= scale;
		calculateMatrix();
	}
	
	public void translate(float dx, float dy) {
		this.offsetX += dx;
		this.offsetY += dy;
	}

	public void setOffset(float x, float y) {
		this.offsetX = x;
		this.offsetY = y;
	}

	public void setCenter(float centerX, float centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}

	protected void calculateMatrix() {
		// Calculates original position by inverting the current matrix.
		// As the matrix incorporates that position, it stores every transformation, even though
		// the matrix is created anew.

		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();
		float originalX = invMatrix.multX(centerX, centerY);
		float originalY = invMatrix.multY(centerX, centerY);

		matrix = new PMatrix3D();
		matrix.translate(centerX, centerY);
		matrix.scale(scale);
		matrix.rotate(angle);
		matrix.translate(-originalX, -originalY);
	}

	protected float getAngleBetween(float x1, float y1, float x2, float y2) {
		float difY = y1 - y2;
		float difX = x1 - x2;
		float angle = PApplet.atan2(difY, difX);
		return angle;
	}

}

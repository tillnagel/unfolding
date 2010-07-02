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

	// test
	boolean nonMatrixOffset = true;

	public TransformableObject(PApplet p, float offsetX, float offsetY, float width, float height) {
		this.p = p;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;

		this.centerX = width / 2;
		this.centerY = height / 2;

		this.color = p.color(255, 150);

		calculateMatrix();
	}

	public void draw() {
		p.pushMatrix();
		if (nonMatrixOffset) {
			p.translate(offsetX, offsetY);
		}
		p.applyMatrix(matrix);

		internalDraw();

		p.popMatrix();
	}

	public void internalDraw() {
		p.fill(color);
		p.stroke(0, 150);

		for (float i = 0; i <= width - 10; i += 10) {
			for (float j = 0; j <= height - 10; j += 10) {
				p.rect(i, j, 10, 10);
			}
		}

		p.fill(255);
		p.rect(0, 0, 50, 20);
		p.fill(0);
		p.text(">>> ABC >>>", 0, 13);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isHit(int checkX, int checkY) {
		float[] check = getTransformedPosition(checkX, checkY);
		return (check[0] > 0 && check[0] < 0 + width && check[1] > 0 && check[1] < 0 + height);
	}

	public float[] getTransformedPosition(float x, float y) {
		return getTransformedPositionWithoutOffset(x - offsetX, y - offsetY, true);
	}
	
	public float[] getTransformedPositionWithoutOffset(float x, float y, boolean inverse) {
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

	public void addOffset(float dx, float dy) {
		this.offsetX += dx;
		this.offsetY += dy;
		calculateMatrix();
	}

	public void setOffset(float x, float y) {
		this.offsetX = x;
		this.offsetY = y;
		calculateMatrix();
	}

	public void setCenter(float centerX, float centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}

	protected void calculateMatrix() {
		if (nonMatrixOffset) {
			// Matrix does not use offset, is used from outside.
			calculateMatrixSingleMatrixNoOffset();
		} else {
			// Uses offset, but is changed only after other transformations.
			calculateMatrixSingleMatrix();
		}

		// Trial with two matrices.
		// calculateMatrixWithNewMatrix();
	}

	// 
	protected void calculateMatrixSingleMatrixNoOffset() {
		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();

		float originalCenterX = invMatrix.multX(centerX, centerY);
		float originalCenterY = invMatrix.multY(centerX, centerY);

		matrix = new PMatrix3D();
		matrix.translate(centerX, centerY);
		matrix.scale(scale);
		matrix.rotate(angle);
		matrix.translate(-originalCenterX, -originalCenterY);
	}

	boolean first = true;
	float oldOffsetX;
	float oldOffsetY;

	protected void calculateMatrixSingleMatrix() {
		if (first) {
			first = false;
		} else {
			matrix.translate(-oldOffsetX, -oldOffsetY);
		}

		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();

		float originalCenterX = invMatrix.multX(centerX, centerY);
		float originalCenterY = invMatrix.multY(centerX, centerY);

		matrix = new PMatrix3D();
		matrix.translate(centerX, centerY);
		matrix.scale(scale);
		matrix.rotate(angle);
		matrix.translate(-originalCenterX, -originalCenterY);
		matrix.translate(offsetX, offsetY);

		oldOffsetX = offsetX;
		oldOffsetY = offsetY;
	}

	PMatrix3D matrix2 = new PMatrix3D();

	protected void calculateMatrixWithNewMatrix() {
		if (first) {
			first = false;
		} else {
			matrix.translate(-oldOffsetX, -oldOffsetY);
		}

		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix2);
		invMatrix.invert();

		float originalCenterX = invMatrix.multX(centerX, centerY);
		float originalCenterY = invMatrix.multY(centerX, centerY);

		matrix2 = new PMatrix3D();
		matrix2.translate(centerX, centerY);
		matrix2.scale(scale);
		matrix2.rotate(angle);
		matrix2.translate(-originalCenterX, -originalCenterY);

		matrix = new PMatrix3D();
		matrix.translate(offsetX, offsetY);
		matrix.apply(matrix2);

		oldOffsetX = offsetX;
		oldOffsetY = offsetY;
	}

	protected float getAngleBetween(float x1, float y1, float x2, float y2) {
		float difY = y1 - y2;
		float difX = x1 - x2;
		float angle = PApplet.atan2(difY, difX);
		return angle;
	}

}

package de.fhpotsdam.transmatrix;

import processing.core.PApplet;
import processing.core.PMatrix3D;

/**
 * Simple drawing object. Used instead of a map to solely check transformations.
 * 
 */
public class TransformableObject {

	PApplet p;

	/** Offset x. */
	protected float offsetX;
	/** Offset y. */
	protected float offsetY;

	/** Width of this object */
	float width;
	/** Height of this object */
	float height;

	/** Transformation center, to rotate and scale around. */
	float transCenterX;
	float transCenterY;

	/** Rotation angle */
	float angle = 0;
	/** Scaling factor */
	float scale = 1;

	int color;

	PMatrix3D matrix = new PMatrix3D();

	public TransformableObject(PApplet p, float offsetX, float offsetY, float width, float height) {
		this.p = p;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;

		this.transCenterX = width / 2;
		this.transCenterY = height / 2;

		this.color = p.color(255, 150);

		calculateMatrix();
	}

	public void draw() {
		p.pushMatrix();
		p.translate(offsetX, offsetY);
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
		p.textSize(12);
		p.text(">>> ABC >>>", 0, 13);
	}

	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * Checks whether given screen coordinates are hitting this object.
	 * 
	 * @param checkX
	 * @param checkY
	 * @return
	 */
	public boolean isHit(int checkX, int checkY) {
		float[] check = getObjectFromScreenPosition(checkX, checkY);
		return (check[0] > 0 && check[0] < 0 + width && check[1] > 0 && check[1] < 0 + height);
	}

	public float[] getObjectFromScreenPosition(float x, float y) {
		return getTransformedPosition(x, y, true);
	}

	public float[] getScreenFromObjectPosition(float x, float y) {
		return getTransformedPosition(x, y, false);
	}

	private float[] getTransformedPosition(float x, float y, boolean inverse) {
		if (inverse) {
			x -= offsetX;
			y -= offsetY;
		}

		float[] xy = new float[3];
		PMatrix3D m = new PMatrix3D();
		m.apply(matrix);
		if (inverse) {
			m.invert();
		}
		m.mult(new float[] { x, y, 0 }, xy);

		if (!inverse) {
			xy[0] += offsetX;
			xy[1] += offsetY;
		}

		return xy;
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

	public void setTransformationCenter(float centerX, float centerY) {
		this.transCenterX = centerX - offsetX;
		this.transCenterY = centerY - offsetY;
	}

	protected void calculateMatrix() {
		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();

		float originalCenterX = invMatrix.multX(transCenterX, transCenterY);
		float originalCenterY = invMatrix.multY(transCenterX, transCenterY);

		matrix = new PMatrix3D();
		matrix.translate(transCenterX, transCenterY);
		matrix.scale(scale);
		matrix.rotate(angle);
		matrix.translate(-originalCenterX, -originalCenterY);
	}

	protected float getAngleBetween(float x1, float y1, float x2, float y2) {
		float difY = y1 - y2;
		float difX = x1 - x2;
		float angle = PApplet.atan2(difY, difX);
		return angle;
	}

}

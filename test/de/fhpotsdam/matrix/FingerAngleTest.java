package de.fhpotsdam.matrix;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

/**
 * Simple test to calculate angle between fingers.
 *
 */
public class FingerAngleTest extends PApplet implements TuioListener {

	TuioClient tuioClient;
	TuioCursor tuioCursor1 = null;
	TuioCursor tuioCursor2 = null;

	float oldAngle;

	public void setup() {
		size(1000, 800);
		smooth();
		
		textFont(loadFont("Miso-Light-12.vlw"), 24);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();
	}

	public void draw() {
		background(240);
		
		fill(255);
		rect(100, 100, 600, 600);
		
		fill(0);
		text(degrees(oldAngle) + " deg", 100, 100);
		
		text(degrees(getAngleBetween(600, 600, 100, 100)), 200, 100);

		if (tuioCursor1 != null && tuioCursor2 != null) {
			line(tuioCursor1.getScreenX(width), tuioCursor1.getScreenY(height), tuioCursor2
					.getScreenX(width), tuioCursor2.getScreenY(height));
		}
	}

	public void addTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 == null) {
			tuioCursor1 = tcur;
		} else if (tuioCursor2 == null) {
			tuioCursor2 = tcur;
			oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 != null && tuioCursor2 != null) {
			float newAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			float angle = newAngle - oldAngle;
			oldAngle = newAngle;
		}
	}

	private float getAngleBetween(float x1, float y1, float x2, float y2) {
		float difY = y1 - y2;
		float difX = x1 - x2;
		float angle = atan2(difY, difX);
		return angle;
	}

	private float getAngleBetween(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return getAngleBetween(tuioCursor1.getScreenX(width), tuioCursor1.getScreenY(height), tuioCursor2
				.getScreenX(width), tuioCursor2.getScreenY(height));
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
	}

	@Override
	public void refresh(TuioTime arg0) {
	}

	@Override
	public void removeTuioCursor(TuioCursor arg0) {
	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
	}

}

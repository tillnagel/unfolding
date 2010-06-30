package de.fhpotsdam.transmatrix;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import TUIO.TuioCursor;

public class TuioTransformableObject extends TransformableObject {

	public static Logger log = Logger.getLogger(TuioTransformableObject.class);

	TuioCursor tuioCursor1 = null;
	TuioCursor tuioCursor2 = null;

	float oldAngle;
	float oldDist;

	float oldX;
	float oldY;

	public TuioTransformableObject(PApplet p, float offsetX, float offsetY, float width,
			float height) {
		super(p, offsetX, offsetY, width, height);
	}

	protected float getDistance(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return PApplet.dist(tuioCursor1.getScreenX(p.width), tuioCursor1.getScreenY(p.height),
				tuioCursor2.getScreenX(p.width), tuioCursor2.getScreenY(p.height));
	}

	protected float getAngleBetween(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return getAngleBetween(tuioCursor1.getScreenX(p.width), tuioCursor1.getScreenY(p.height),
				tuioCursor2.getScreenX(p.width), tuioCursor2.getScreenY(p.height));
	}

	public void addTuioCursor(TuioCursor tuioCursor) {
		if (tuioCursor1 == null) {
			tuioCursor1 = tuioCursor;
//			oldX = tuioCursor1.getScreenX(p.width);
//			oldY = tuioCursor1.getScreenY(p.height);

			log.debug("Set 1st cursor: " + tuioCursor.getCursorID());
		} else if (tuioCursor2 == null) {
			tuioCursor2 = tuioCursor;
			log.debug("Set 2nd cursor: " + tuioCursor.getCursorID());

			oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			oldDist = getDistance(tuioCursor1, tuioCursor2);
		} else {
			log.info("Already 2 cursors in use for rotation");
		}

		log.debug("tc1: " + tuioCursor1);
		log.debug("tc2: " + tuioCursor2);
		if (tuioCursor1 != null || tuioCursor2 != null) {
			setColor(p.color(255, 0, 0, 150));
		} else {
			setColor(p.color(255, 150));
		}
	}

	public void removeTuioCursor(TuioCursor tuioCursor) {
		log.debug("Removing: " + tuioCursor.getCursorID());

		if (tuioCursor2 != null && tuioCursor2.getCursorID() == tuioCursor.getCursorID()) {
			tuioCursor2 = null;
		}

		if (tuioCursor1 != null && tuioCursor1.getCursorID() == tuioCursor.getCursorID()) {
			tuioCursor1 = null;
			if (tuioCursor2 != null) {
				tuioCursor1 = tuioCursor2;
				tuioCursor2 = null;
			}
		}

		log.debug("tc1: " + tuioCursor1);
		log.debug("tc2: " + tuioCursor2);
		if (tuioCursor1 != null || tuioCursor2 != null) {
			setColor(p.color(255, 0, 0, 150));
		} else {
			setColor(p.color(255, 150));
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 != null && tuioCursor2 != null) {
			// Two fingers: rotate and scale

			if (tuioCursor2.getCursorID() == tcur.getCursorID()) {
				centerX = tuioCursor1.getScreenX(p.width);
				centerY = tuioCursor1.getScreenY(p.height);
			} else {
				centerX = tuioCursor2.getScreenX(p.width);
				centerY = tuioCursor2.getScreenY(p.height);
			}

			float newAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			float angle = newAngle - oldAngle;
			oldAngle = newAngle;
			rotate(angle);

			float newDist = getDistance(tuioCursor1, tuioCursor2);
			float newScale = newDist / oldDist;
			oldDist = newDist;
			scale(newScale);

		} else if (tuioCursor1 != null) {
			// One finger: move
//			float x = tuioCursor1.getScreenX(p.width);
//			float y = tuioCursor1.getScreenY(p.height);
//			float dx = x - oldX;
//			float dy = y - oldY;
//
//			translate(dx, dy);
//
//			oldX = x;
//			oldY = y;
		}
	}

}

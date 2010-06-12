package de.fhpotsdam.unfolding.examples.interaction;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PVector;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * 
 * TODO Combination with zooming mandatory, to match finger positions.
 * 
 * DONE Rotate around some spot between the two fingers (not around center). E.g. one finger fixed,
 * one finger moving should result in rotating around fixed finger.
 * 
 * TODO Add map.isActive / hit test to finger interaction.
 * 
 */
public class FingerRotateMapApp extends PApplet implements TuioListener {

	public static Logger log = Logger.getLogger(FingerRotateMapApp.class);

	TuioClient tuioClient;
	Map map;

	TuioCursor tuioCursor1 = null;
	TuioCursor tuioCursor2 = null;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();

		map = new Map(this, "map1", 50, 50, 500, 400);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(loadFont("Miso-Light-12.vlw"), 12);

		// map.rotate(radians(-45));
	}

	float startAngle = 0;

	public void draw() {
		background(0);

		map.draw();

		drawCursor(tuioCursor1);
		drawCursor(tuioCursor2);
	}

	public void addTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 == null) {
			tuioCursor1 = tcur;
		} else if (tuioCursor2 == null) {
			tuioCursor2 = tcur;

			startAngle = getAngleBetween(tuioCursor1, tuioCursor2);
		} else {
			log.info("Already 2 cursors in use for rotation");
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 != null && tuioCursor2 != null) {
			PVector transCenter;
			if (tuioCursor2.getCursorID() == tcur.getCursorID()) {
				transCenter = new PVector(tuioCursor1.getScreenX(width), tuioCursor1
						.getScreenY(height));
			} else {
				transCenter = new PVector(tuioCursor2.getScreenX(width), tuioCursor2
						.getScreenY(height));
			}

			float newAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			float angle = newAngle - startAngle;
			startAngle = newAngle;
			map.rotate(angle, transCenter);
		}
	}

	public void removeTuioCursor(TuioCursor tcur) {
		if (tuioCursor2 != null && tuioCursor2.getCursorID() == tcur.getCursorID()) {
			tuioCursor2 = null;
		}

		if (tuioCursor1 != null && tuioCursor1.getCursorID() == tcur.getCursorID()) {
			tuioCursor1 = null;
			if (tuioCursor2 != null) {
				tuioCursor1 = tuioCursor2;
				tuioCursor2 = null;
			}
		}
	}

	private float getAngleBetween(float x1, float y1, float x2, float y2) {
		float difY = y1 - y2;
		float difX = x1 - x2;
		float angle = 0;
		if (difX != 0 && difY != 0) {
			angle = atan2(difY, difX);
		}
		return angle;
	}

	private float getAngleBetween(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return getAngleBetween(tuioCursor1.getScreenX(width), tuioCursor1.getScreenY(height),
				tuioCursor2.getScreenX(width), tuioCursor2.getScreenY(height));
	}

	public void drawCursor(TuioCursor tc) {
		if (tc == null)
			return;

		stroke(50, 100);
		fill(230, 150);
		ellipse(tc.getScreenX(width), tc.getScreenY(height), 15, 15);
		fill(10);
		text(tc.getCursorID(), tc.getScreenX(width) - 3, tc.getScreenY(height) + 4);
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(TuioTime arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub

	}

}

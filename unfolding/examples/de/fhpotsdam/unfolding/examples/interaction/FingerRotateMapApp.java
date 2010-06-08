package de.fhpotsdam.unfolding.examples.interaction;

import java.util.Vector;

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
 * TODO Rotate around some spot between the two fingers (not around center). E.g. one finger fixed,
 * one finger moving should result in rotating around fixed finger.
 * 
 * TODO Add map.isActive / hit test to finger interaction.
 * 
 */
public class FingerRotateMapApp extends PApplet implements TuioListener {

	TuioClient tuioClient;
	Map map;
	

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();

		map = new Map(this, "map1", 50, 50, 500, 400);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(loadFont("Miso-Light-12.vlw"), 12);
	}

	float startAngle = 0;

	public void draw() {
		background(0);

		PVector rotateCenter = null;
		Vector<TuioCursor> tuioCursors = tuioClient.getTuioCursors();
		if (tuioCursors.size() == 2) {
			// NB: cursors ordered desc by recency, i.e. [0]=1, [1]=0
			TuioCursor tc = tuioCursors.get(0);
			float newAngle = getAngleBetween(tc, tuioCursors.get(1));
			float angle = newAngle - startAngle;

			rotateCenter = new PVector(tc.getScreenX(width), tc.getScreenY(height));
			map.rotate(angle, rotateCenter);
			// map.rotate(angle, new PVector(200-50, 200-50));
			startAngle = newAngle;
		}

		map.draw();

		if (rotateCenter != null) {
			fill(255, 0, 0, 100);
			ellipse(rotateCenter.x, rotateCenter.y, 15, 15);
		}

		for (TuioCursor tc : tuioCursors) {
			drawCursor(tc);
		}
	}

	public void addTuioCursor(TuioCursor tcur) {
		Vector<TuioCursor> tuioCursors = tuioClient.getTuioCursors();
		if (tuioCursors.size() == 2) {
			startAngle = getAngleBetween(tuioCursors.get(0), tuioCursors.get(1));
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		

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
		return getAngleBetween(tuioCursor1.getX(), tuioCursor1.getY(), tuioCursor2.getX(),
				tuioCursor2.getY());
	}

	public void drawCursor(TuioCursor tc) {
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
	public void removeTuioCursor(TuioCursor arg0) {
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

package de.fhpotsdam.unfolding.examples.interaction;

import java.util.Vector;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.utils.MapUtils;

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
	}

	float startAngle = 0;

	public void draw() {
		background(0);

		Vector<TuioCursor> tuioCursors = tuioClient.getTuioCursors();
		if (tuioCursors.size() == 2) {
			float newAngle = getAngleBetween(tuioCursors.get(0), tuioCursors.get(1));
			float angle = newAngle - startAngle;
			map.rotate(angle);
			startAngle = newAngle;
		}

		map.draw();

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
	public void updateTuioCursor(TuioCursor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub

	}

}

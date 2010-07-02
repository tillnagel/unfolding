package de.fhpotsdam.transmatrix;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import codeanticode.glgraphics.GLConstants;

/**
 * Fully working test app to freely transform multiple independent objects. Rotate, Scale, and
 * translate (via offset, as well as implicit transform by the former two) work.
 * 
 */
public class MultiTransObjectApp extends PApplet implements TuioListener {

	List<TuioTransformableObject> transObjects = new ArrayList<TuioTransformableObject>();

	TuioClient tuioClient;

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);
		smooth();
		textFont(loadFont("Miso-Light-12.vlw"), 12);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();

		transObjects.add(new TuioTransformableObject(this, 0, 0, 300, 300));
		transObjects.add(new TuioTransformableObject(this, 500, 200, 300, 300));
		transObjects.add(new TextThing(this, 600, 50, 340, 70));
	}

	public void draw() {
		background(240);

		for (TransformableObject transformableObject : transObjects) {
			transformableObject.draw();
		}
	}
	
	public void addTuioCursor(TuioCursor tcur) {
		// Hit test for all objects: first gets the hit, ordered by creation.
		// TODO Order by z-index, updated by last activation/usage
		for (TuioTransformableObject ttObj : transObjects) {
			if (ttObj.isHit(tcur.getScreenX(width), tcur.getScreenY(height))) {
				ttObj.addTuioCursor(tcur);
				break;
			}
		}
	}

	public void removeTuioCursor(TuioCursor tcur) {
		for (TuioTransformableObject ttObj : transObjects) {
			// Pass trough remove-event to all objects, to allow fingerUp also out of boundaries,
			// as objects decide themselves (via cursor-id) whether cursor belongs to it.

			ttObj.removeTuioCursor(tcur);
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		for (TuioTransformableObject ttObj : transObjects) {
			if (ttObj.isHit(tcur.getScreenX(width), tcur.getScreenY(height))) {
				ttObj.updateTuioCursor(tcur);
				break;
			}
		}
	}
	
	public void drawCursors() {
		for (TuioCursor tuioCursor : tuioClient.getTuioCursors()) {
			drawCursor(tuioCursor);
		}
	}

	public void drawCursor(TuioCursor tc) {
		if (tc == null)
			return;

		stroke(50, 100);
		fill(230, 150);
		ellipse(tc.getScreenX(width), tc.getScreenY(height), 15, 15);
		fill(10);
		textSize(12);
		text(tc.getCursorID(), tc.getScreenX(width) - 3, tc.getScreenY(height) + 4);
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
	}

	@Override
	public void refresh(TuioTime arg0) {
	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
	}

}

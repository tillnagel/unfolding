package de.fhpotsdam.transmatrix;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import codeanticode.glgraphics.GLConstants;

/**
 * Fully working test app to freely transform an object (Grid). Rotate, Scale (and implicit
 * transform by the former two) work.
 * 
 */
public class MultiTransObjectApp extends PApplet implements TuioListener {

	public static Logger log = Logger.getLogger(MultiTransObjectApp.class);

	List<TuioTransformableObject> transObjects = new ArrayList<TuioTransformableObject>();

	TuioClient tuioClient;

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);
		// size(800, 400, GLConstants.GLGRAPHICS);
		smooth();
		textFont(loadFont("Miso-Light-12.vlw"), 12);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();

		transObjects.add(new TuioTransformableObject(this, 100, 100, 300, 300));
		transObjects.add(new TuioTransformableObject(this, 500, 200, 300, 300));
	}

	public void draw() {
		background(240);

		for (TransformableObject transformableObject : transObjects) {
			transformableObject.draw();
		}

		// Display debug information
		drawCursors();
		fill(0);
		text("fps: " + nfs(frameRate, 3, 3), 20, 20);
	}

	public void keyPressed() {
		transObjects.get(0).setOffset(50, 50);
	}

	public void addTuioCursor(TuioCursor tcur) {
		// Hit test for all objects, ordered by creation
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
			// Pass trough remove to all objects, to allow fingerUp also out of boundaries,
			// as objects decide themselves (via cursor-id) whether cursor belongs to it.
			
			// if (ttObj.isHit(tcur.getScreenX(width), tcur.getScreenY(height))) {
			ttObj.removeTuioCursor(tcur);
			// break;
			// }
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

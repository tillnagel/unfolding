package de.fhpotsdam.transmatrix;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

/**
 * Fully working test app to freely transform multiple independent objects. Rotate, Scale, and
 * translate (via offset, as well as implicit transform by the former two) work.
 * 
 */
public class MultiTransObjectApp extends PApplet implements TuioListener {

	List<TuioTransformableObject> transObjects = new ArrayList<TuioTransformableObject>();

	TuioClient tuioClient;

	// public static void main(String[] args) {
	// PApplet.main(new String[] { "--present", "de.fhpotsdam.transmatrix.MultiTransObjectApp" });
	// }

	public void setup() {
		size(1024, 768, OPENGL);
		// size(1920, 1080, OPENGL);

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

		// Display debug information
		drawCursors();
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

	// Test interactions ----------------------------------

	public void mouseWheel(float delta) {
		TransformableObject to = null;
		for (TuioTransformableObject ttObj : transObjects) {
			if (ttObj.isHit(mouseX, mouseY)) {
				to = ttObj;
				break;
			}
		}
		if (to != null) {
			to.setTransformationCenter(mouseX, mouseY);
			if (delta < 0) {
				to.scale(0.9f);
			} else {
				to.scale(1.1f);
			}
		}
	}

	public void mouseMoved() {
		// TransformableObject to = transObjects.get(0);
		// if (to.isHit(mouseX, mouseY)) {
		// to.setColor(color(255, 0, 0, 100));
		// } else {
		// to.setColor(color(255, 100));
		// }
	}

	public void keyPressed() {
		TransformableObject to = transObjects.get(0);

		if (key == 'r') {
			println("rotate cw");
			to.rotate(radians(45));
		}
		if (key == 'l') {
			println("rotate ccw");
			to.rotate(-radians(45));
		}
		if (key == 'm') {
			println("using mouse-pos as center: " + mouseX + "," + mouseY);
			// use world coords
			to.setTransformationCenter(mouseX, mouseY);
		}

		if (key == 'c') {
			// get world coords from object/scene coords
			float[] check = to.getScreenFromObjectPosition(300, 300);
			to.setTransformationCenter(check[0], check[1]);

			println("using transPos for center: " + check[0] + "," + check[1]);

		}
		if (key == 'C') {
			float[] check = to.getScreenFromObjectPosition(0, 0);
			println("using transPos for center: " + check[0] + "," + check[1]);
			to.setTransformationCenter(check[0], check[1]);
		}

		if (key == CODED) {
			if (keyCode == LEFT) {
				println("left");
				to.addOffset(-100, 0);
			}
			if (keyCode == RIGHT) {
				println("right");
				to.addOffset(100, 0);
			}
		}

		if (key == 's') {
			to.scale(0.95f);
		}
		if (key == 'S') {
			to.scale(1.05f);
		}

		if (key == 'i') {
			float[] check = to.getScreenFromObjectPosition(0, 0);
			println("0,0: " + check[0] + "," + check[1]);
			println("ctr: " + to.transCenterX + "," + to.transCenterY);
			println(mouseX + " " + mouseY);
		}
	}

	public void mouseDragged() {
		float dx = mouseX - pmouseX;
		float dy = mouseY - pmouseY;
		transObjects.get(0).addOffset(dx, dy);

		// transObjects.get(0).setOffset(mouseX, mouseY);
	}
}

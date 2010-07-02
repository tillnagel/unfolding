package de.fhpotsdam.transmatrix;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import codeanticode.glgraphics.GLConstants;

/**
 * Fully working test app to freely transform multiple independent objects. Rotate, Scale (and
 * implicit transform by the former two) work.
 * 
 * Not working: Transform, i.e. changing the offset.
 * 
 */
public class MultiTransObjectApp extends PApplet implements TuioListener {

	public static Logger log = Logger.getLogger(MultiTransObjectApp.class);

	List<TuioTransformableObject> transObjects = new ArrayList<TuioTransformableObject>();

	TuioClient tuioClient;

	public void setup() {
		// size(1024, 768, GLConstants.GLGRAPHICS);
		size(800, 400, GLConstants.GLGRAPHICS);
		smooth();
		textFont(loadFont("Miso-Light-12.vlw"), 12);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();

		transObjects.add(new TuioTransformableObject(this, 0, 0, 300, 300));
		// transObjects.add(new TuioTransformableObject(this, 500, 200, 300, 300));
	}

	public void draw() {
		background(240);

		for (TransformableObject transformableObject : transObjects) {
			transformableObject.draw();
		}

		// Display debug information
		drawCursors();
		fill(0);
		text("fps: " + nfs(frameRate, 2, 3), 20, 20);

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
			to.centerX = mouseX;
			to.centerY = mouseY;
		}

		if (key == 'c') {
			float[] check = to.getTransformedPosition(300, 300, false);
			println("using transPos for center: " + check[0] + "," + check[1]);
			to.setCenter(check[0], check[1]);
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
			float[] check = to.getTransformedPosition(0, 0, false);
			println("0,0: " + check[0] + "," + check[1]);
			println("ctr: " + to.centerX + "," + to.centerY);
		}
	}

	public String getMatrix3DAsString(PMatrix3D m) {
		return nfs(m.m00, 2, 2) + "," + nfs(m.m01, 2, 2) + "," + nfs(m.m02, 2, 2) + ","
				+ nfs(m.m03, 2, 2) + "\n" + nfs(m.m10, 2, 2) + "," + nfs(m.m11, 2, 2) + ","
				+ nfs(m.m12, 2, 2) + "," + nfs(m.m13, 2, 2) + "\n" + nfs(m.m20, 2, 2) + ","
				+ nfs(m.m21, 2, 2) + "," + nfs(m.m22, 2, 2) + "," + nfs(m.m33, 2, 2) + "\n"
				+ nfs(m.m30, 2, 2) + "," + nfs(m.m31, 2, 2) + "," + nfs(m.m32, 2, 2) + ","
				+ nfs(m.m33, 2, 2) + "\n";
	}

	public void mouseDragged() {
		float dx = mouseX - pmouseX;
		float dy = mouseY - pmouseY;
		transObjects.get(0).addOffset(dx, dy);

		// transObjects.get(0).setOffset(mouseX, mouseY);
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

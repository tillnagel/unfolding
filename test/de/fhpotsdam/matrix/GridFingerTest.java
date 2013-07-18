package de.fhpotsdam.matrix;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;


/**
 * Fully working test app to freely transform an object (Grid). Rotate, Scale (and implicit
 * transform by the former two) work.
 * 
 */
public class GridFingerTest extends PApplet implements TuioListener {

	public static Logger log = Logger.getLogger(GridFingerTest.class);

	Grid grid;

	TuioClient tuioClient;

	TuioCursor tuioCursor1 = null;
	TuioCursor tuioCursor2 = null;

	float oldAngle;
	float oldDist;

	// public static void main(String[] args) {
	// PApplet.main(new String[] { "--present", "de.fhpotsdam.matrix.GridFingerTest" });
	// }

	public void setup() {
		size(1024, 768, OPENGL);
		smooth();

		grid = new Grid(this, 100, 100, 300, 300);
		textFont(loadFont("Miso-Light-12.vlw"), 12);

		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();
	}

	public void draw() {
		background(240);
		
		grid.draw();
		
		drawCursor(tuioCursor1);
		drawCursor(tuioCursor2);
	}

	public void addTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 == null) {
			tuioCursor1 = tcur;
		} else if (tuioCursor2 == null) {
			tuioCursor2 = tcur;

			oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			oldDist = getDistance(tuioCursor1, tuioCursor2);
		} else {
			log.info("Already 2 cursors in use for rotation");
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		if (tuioCursor1 != null && tuioCursor2 != null) {
			if (tuioCursor2.getCursorID() == tcur.getCursorID()) {
				grid.centerX = tuioCursor1.getScreenX(width);
				grid.centerY = tuioCursor1.getScreenY(height);
			} else {
				grid.centerX = tuioCursor2.getScreenX(width);
				grid.centerY = tuioCursor2.getScreenY(height);
			}

			float newAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			float angle = newAngle - oldAngle;
			oldAngle = newAngle;
			grid.rotate(angle);

			float newDist = getDistance(tuioCursor1, tuioCursor2);
			float newScale = newDist / oldDist;
			oldDist = newDist;
			grid.scale(newScale);

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
		float angle = atan2(difY, difX);
		return angle;
	}

	private float getDistance(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return dist(tuioCursor1.getScreenX(width), tuioCursor1.getScreenY(height), tuioCursor2
				.getScreenX(width), tuioCursor2.getScreenY(height));
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

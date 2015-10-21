package de.fhpotsdam.unfolding.interactions;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioPoint;
import TUIO.TuioTime;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEventBroadcaster;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Handles finger input (TUIO cursors) from the user and broadcasts MapEvents such as zoom and pan.
 */
// FIXME Implement tuio to map interactions as events!
// REVISIT Check how to use simpletouch's TuioTransformableObject. For use in events, as in here.
public class TuioCursorHandler extends MapEventBroadcaster implements TuioListener {

	public static Logger log = Logger.getLogger(TuioCursorHandler.class);

	private PApplet p;

	protected TuioClient tuioClient;

	TuioCursor tuioCursor1;
	TuioCursor tuioCursor2;

	Stack<TuioCursor> unusedTuioCursorStack = new Stack<TuioCursor>();

	float oldAngle;
	float oldDist;
	PFont font;

	public TuioCursorHandler(PApplet p, UnfoldingMap... maps) {
		this(p, Arrays.asList(maps));
	}

	public TuioCursorHandler(PApplet p, boolean listenToTuio, UnfoldingMap... maps) {
		this(p, listenToTuio, Arrays.asList(maps));
	}

	public TuioCursorHandler(PApplet p, boolean listenToTuio, List<UnfoldingMap> maps) {
		super(maps);

		this.p = p;
		this.font = p.createFont("Sans-Serif", 12);

		tuioClient = new TuioClient();
		if (listenToTuio) {
			tuioClient.addTuioListener(this);
		}
		tuioClient.connect();

                p.registerMethod("dispose", this);
	}

	public TuioCursorHandler(PApplet p, List<UnfoldingMap> maps) {
		this(p, true, maps);
	}

	public void dispose() {
		tuioClient.disconnect();
	}

	public TuioClient getTuioClient() {
		return tuioClient;
	}

	public void updateTuioCursor(TuioCursor tcur) {
		int x = tcur.getScreenX(p.width);
		int y = tcur.getScreenY(p.height);

		// Updates go to all hit ones, independent of z-index
		for (UnfoldingMap map : maps) {
			if (map.isHit(x, y)) {

				if (tuioCursor1 != null && tuioCursor2 != null) {
					// Two fingers: Zoom + Rotate
					// FIXME In very high zoom levels zooming is off. Precision problem?
					// (float/double)

					// Uses inner zoom and inner rotation, thus fingers are on same location

					// Flags to test various combinations
					boolean zoom = true;
					boolean rotate = true;

					PVector transCenter = new PVector();
					if (tuioCursor2.getCursorID() == tcur.getCursorID()) {
						transCenter.x = tuioCursor1.getScreenX(p.width);
						transCenter.y = tuioCursor1.getScreenY(p.height);
					} else {
						transCenter.x = tuioCursor2.getScreenX(p.width);
						transCenter.y = tuioCursor2.getScreenY(p.height);
					}

					if (zoom) {
						ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId(), ZoomMapEvent.ZOOM_BY);
						
						// 3 variations to zoom with two finger gestures
						// TODO Do study on usability for different interaction approaches.
						
						// 1. pos of last finger
						Location centerLocation = map.getLocation(transCenter.x, transCenter.y);
						zoomMapEvent.setTransformationCenterLocation(centerLocation);

						// 2. object center: pinch gesture w/o fixed finger-location connection
						// float[] objectCenterXY =
						// map.mapDisplay.getScreenFromObjectPosition(map.mapDisplay.getWidth()/2,
						// map.mapDisplay.getHeight()/2);
						// PVector objectCenter = new PVector(objectCenterXY[0], objectCenterXY[1]);
						// map.mapDisplay.setInnerTransformationCenter(objectCenter);

						// 3. middle pos between both fingers

						float newDist = getDistance(tuioCursor1, tuioCursor2);
						float scaleDelta = newDist / oldDist;
						oldDist = newDist;
						zoomMapEvent.setZoomDelta(scaleDelta);

						eventDispatcher.fireMapEvent(zoomMapEvent);
					}

					if (rotate) {
						
						// TODO Use events (instead of direct map manipulation)

						// rotate center
						map.mapDisplay.setTransformationCenter(transCenter);

						float newAngle = getAngleBetween(tuioCursor1, tuioCursor2);
						float angle = newAngle - oldAngle;
						oldAngle = newAngle;
						map.rotate(angle);
					}


				} else if (tuioCursor1 != null) {
					// One finger: pan

					if (tuioCursor1.getCursorID() == tcur.getCursorID()) {
						TuioPoint oldTuioPoint = tcur.getPath().get(tcur.getPath().size() - 2);
						Location fromLocation = map.mapDisplay.getLocation(oldTuioPoint.getScreenX(p.width),
								oldTuioPoint.getScreenY(p.height));
						Location toLocation = map.mapDisplay.getLocation(tuioCursor1.getScreenX(p.width),
								tuioCursor1.getScreenY(p.height));

						PanMapEvent panMapEvent = new PanMapEvent(this, map.getId(), PanMapEvent.PAN_BY);
						panMapEvent.setFromLocation(fromLocation);
						panMapEvent.setToLocation(toLocation);
						eventDispatcher.fireMapEvent(panMapEvent);
					}
				}
			}
		}
	}

	public void addTuioCursor(TuioCursor tuioCursor) {
		if (tuioCursor1 == null) {
			tuioCursor1 = tuioCursor;
		} else if (tuioCursor2 == null) {
			tuioCursor2 = tuioCursor;

			oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			oldDist = getDistance(tuioCursor1, tuioCursor2);
		} else {
			// PApplet.println("Already 2 cursors in use. Adding further ones to stack.");
			unusedTuioCursorStack.add(tuioCursor);
		}
	}

	public void removeTuioCursor(TuioCursor tuioCursor) {
		if (tuioCursor2 != null && tuioCursor2.getCursorID() == tuioCursor.getCursorID()) {
			tuioCursor2 = null;

			if (!unusedTuioCursorStack.isEmpty()) {
				tuioCursor2 = unusedTuioCursorStack.pop();
				oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
				oldDist = getDistance(tuioCursor1, tuioCursor2);
			}

		}

		if (tuioCursor1 != null && tuioCursor1.getCursorID() == tuioCursor.getCursorID()) {
			tuioCursor1 = null;
			// If second still is on object, switch cursors
			if (tuioCursor2 != null) {
				tuioCursor1 = tuioCursor2;

				if (!unusedTuioCursorStack.isEmpty()) {
					tuioCursor2 = unusedTuioCursorStack.pop();
					oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
					oldDist = getDistance(tuioCursor1, tuioCursor2);
				} else {
					tuioCursor2 = null;
				}
			}
		}

		// TODO check only if the other were no hits
		TuioCursor toRemoveTC = null;
		for (TuioCursor unusedTuioCursor : unusedTuioCursorStack) {
			if (tuioCursor.getCursorID() == unusedTuioCursor.getCursorID()) {
				toRemoveTC = tuioCursor;
				break;
			}
		}
		if (toRemoveTC != null) {
			unusedTuioCursorStack.remove(toRemoveTC);
		}

	}

	protected float getDistance(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return PApplet.dist(tuioCursor1.getScreenX(p.width), tuioCursor1.getScreenY(p.height),
				tuioCursor2.getScreenX(p.width), tuioCursor2.getScreenY(p.height));
	}

	protected float getAngleBetween(TuioCursor tuioCursor1, TuioCursor tuioCursor2) {
		return getAngleBetween(tuioCursor1.getScreenX(p.width), tuioCursor1.getScreenY(p.height),
				tuioCursor2.getScreenX(p.width), tuioCursor2.getScreenY(p.height));
	}

	protected float getAngleBetween(float x1, float y1, float x2, float y2) {
		float difY = y1 - y2;
		float difX = x1 - x2;
		float angle = PApplet.atan2(difY, difX);
		return angle;
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
	}

	@Override
	public void refresh(TuioTime arg0) {
	}

	/**
	 * Draws all TuioCursors.
	 */
	public void drawCursors() {
		for (TuioCursor tuioCursor : tuioClient.getTuioCursors()) {
			drawCursor(tuioCursor);
		}
	}

	/**
	 * Draws a TuioCursor as small circle with ID as label.
	 * 
	 * @param tc
	 *            The cursor to draw.
	 */
	public void drawCursor(TuioCursor tc) {
		if (tc == null)
			return;

		p.textFont(font);
		// p.stroke(0, 20);
		p.noStroke();
		p.fill(80, 30);
		p.ellipse(tc.getScreenX(p.width), tc.getScreenY(p.height), 27, 27);
		p.fill(80, 80);
		p.ellipse(tc.getScreenX(p.width), tc.getScreenY(p.height), 25, 25);
		p.fill(0, 200);
		p.textSize(12);
		p.text(tc.getCursorID(), tc.getScreenX(p.width) - 4, tc.getScreenY(p.height) + 4);
	}

}

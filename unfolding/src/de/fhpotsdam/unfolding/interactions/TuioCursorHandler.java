package de.fhpotsdam.unfolding.interactions;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.MapEventBroadcaster;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.geo.Location;

// FIXME Implement tuio to map interactions as events!
// REVISIT Check how to use simpletouch's TuioTransformableObject. For use in events, as in here.
public class TuioCursorHandler extends MapEventBroadcaster implements TuioListener {

	public static Logger log = Logger.getLogger(TuioCursorHandler.class);

	private PApplet p;

	protected TuioClient tuioClient;

	TuioCursor tuioCursor1;
	TuioCursor tuioCursor2;
	float oldX, oldY;
	float oldAngle;
	float oldDist;
	PFont font;

	public TuioCursorHandler(PApplet p, Map... maps) {
		this(p, Arrays.asList(maps));
	}

	public TuioCursorHandler(PApplet p, boolean listenToTuio, Map... maps) {
		this(p, listenToTuio, Arrays.asList(maps));
	}

	public TuioCursorHandler(PApplet p, boolean listenToTuio, List<Map> maps) {
		super(maps);

		this.p = p;
		this.font = p.createFont("Sans-Serif", 12);

		tuioClient = new TuioClient();
		if (listenToTuio) {
			tuioClient.addTuioListener(this);
		}
		tuioClient.connect();

		p.registerDispose(this);
	}
	
	public TuioCursorHandler(PApplet p, List<Map> maps) {
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
		for (Map map : maps) {
			if (map.isHit(x, y)) {

				if (tuioCursor1 != null && tuioCursor2 != null) {
					// Two fingers: Zoom + Rotate
					// FIXME In very high zoom levels zooming is off. Precision problem?
					// (float/double)

					// Uses inner zoom and inner rotation, thus fingers are on same location
					// TODO Do study on usability for different interaction approaches. 

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
						// 1. pos of last finger
						map.mapDisplay.setInnerTransformationCenter(transCenter);

						// 2. object center: pinch gesture w/o fixed finger-location connection
						// float[] objectCenterXY =
						// map.mapDisplay.getScreenFromObjectPosition(map.mapDisplay.getWidth()/2,
						// map.mapDisplay.getHeight()/2);
						// PVector objectCenter = new PVector(objectCenterXY[0], objectCenterXY[1]);
						// map.mapDisplay.setInnerTransformationCenter(objectCenter);

						// TODO 3. middle pos between both fingers

						float newDist = getDistance(tuioCursor1, tuioCursor2);
						float scaleDelta = newDist / oldDist;
						oldDist = newDist;
						map.zoom(scaleDelta);
					}

					if (rotate) {
						// rotate center
						map.mapDisplay.setTransformationCenter(transCenter);

						float newAngle = getAngleBetween(tuioCursor1, tuioCursor2);
						float angle = newAngle - oldAngle;
						oldAngle = newAngle;
						map.rotate(angle);
					}

					// TODO Use events (instead of direct map manipulation)
					// ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId());
					// zoomMapEvent.setTransformationCenterLocation(transformationCenterLocation);

				} else if (tuioCursor1 != null) {
					// One finger: pan

					Location fromLocation = map.mapDisplay
							.getLocationFromScreenPosition(oldX, oldY);
					Location toLocation = map.mapDisplay.getLocationFromScreenPosition(x, y);

					PanMapEvent panMapEvent = new PanMapEvent(this, map.getId(), PanMapEvent.PAN_BY);
					panMapEvent.setFromLocation(fromLocation);
					panMapEvent.setToLocation(toLocation);
					eventDispatcher.fireMapEvent(panMapEvent);

					oldX = x;
					oldY = y;
				}
			}
		}
	}

	public void addTuioCursor(TuioCursor tuioCursor) {
		if (tuioCursor1 == null) {
			tuioCursor1 = tuioCursor;

			oldX = tuioCursor1.getScreenX(p.width);
			oldY = tuioCursor1.getScreenY(p.height);

		} else if (tuioCursor2 == null) {
			tuioCursor2 = tuioCursor;

			oldAngle = getAngleBetween(tuioCursor1, tuioCursor2);
			oldDist = getDistance(tuioCursor1, tuioCursor2);
		} else {
			PApplet.println("Already 2 cursors in use for rotation. Omitting further ones.");
		}
	}

	public void removeTuioCursor(TuioCursor tuioCursor) {
		if (tuioCursor2 != null && tuioCursor2.getCursorID() == tuioCursor.getCursorID()) {
			tuioCursor2 = null;
			oldX = tuioCursor1.getScreenX(p.width);
			oldY = tuioCursor1.getScreenY(p.height);
		}

		if (tuioCursor1 != null && tuioCursor1.getCursorID() == tuioCursor.getCursorID()) {
			tuioCursor1 = null;
			// If second still is on object, switch cursors
			if (tuioCursor2 != null) {
				tuioCursor1 = tuioCursor2;
				tuioCursor2 = null;
				// Shall not jump after switching, so a "new" oldPos is stored for diff calc.
				oldX = tuioCursor1.getScreenX(p.width);
				oldY = tuioCursor1.getScreenY(p.height);
			}
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
		//p.stroke(0, 20);
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

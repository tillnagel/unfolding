package de.fhpotsdam.unfolding.examples.interaction.multitouch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Multitouch map which uses single touch for sketching on the map.
 * 
 * See simpler {@link MultitouchMapApp} if you want to use multitouch interaction for the map only.
 */
public class SketchingOnMultitouchMapApp extends PApplet implements TuioListener {

	public static Logger log = Logger.getLogger(SketchingOnMultitouchMapApp.class);

	UnfoldingMap map;
	EventDispatcher eventDispatcher;
	TuioCursorHandler tuioCursorHandler;
	TuioClient tuioClient;

	TuioCursor sketchTuioCursor;
	// Save sketched points as location so it stays consistent with the interactive map
	List<Location> locations = new ArrayList<Location>();

	public static void main(String[] args) {
		String[] params = new String[] { "--present", "--bgcolor=#000000", "--hide-stop",
				"de.fhpotsdam.unfolding.examples.interaction.multitouch.SketchingOnMultitouchMapApp" };
		PApplet.main(params);
	}

	public void settings() {
		size(800, 600, P2D);
		// size(1920, 1080, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.setTweening(false);
		map.zoomAndPanTo(13, new Location(54.5, 13.4));

		eventDispatcher = new EventDispatcher();

		tuioCursorHandler = new TuioCursorHandler(this, false, map);
		eventDispatcher.addBroadcaster(tuioCursorHandler);
		// Disable panning for single touch, to be able to handle it independently
		// eventDispatcher.register(map, "pan");
		eventDispatcher.register(map, "zoom");

		tuioClient = tuioCursorHandler.getTuioClient();
		tuioClient.addTuioListener(this);
	}

	public void draw() {
		map.draw();

		fill(20, 200, 20, 140);
		stroke(0, 200, 0, 200);
		strokeWeight(3);
		beginShape();
		synchronized (locations) {
			for (Location location : locations) {
				ScreenPosition pos = map.getScreenPosition(location);
				vertex(pos.x, pos.y);
			}
		}
		endShape();

		// fill(255, 40, 40, 120);
		// noStroke();
		// for (TuioCursor tcur : tuioClient.getTuioCursors()) {
		// ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 20, 20);
		// }
	}

	@Override
	public void addTuioCursor(TuioCursor tuioCursor) {
		int x = tuioCursor.getScreenX(width);
		int y = tuioCursor.getScreenY(height);
		// log.debug("Add " + tuioCursor.getCursorID() + ": " + x + ", " + y);

		if (sketchTuioCursor == null) {
			// First finger: Use for sketching
			sketchTuioCursor = tuioCursor;
			Location location = map.getLocation(x, y);
			synchronized (locations) {
				locations.add(location);
			}
		} else {
			// Second finger: remove sketching cursor
			sketchTuioCursor = null;
		}

		tuioCursorHandler.addTuioCursor(tuioCursor);
	}

	@Override
	public void updateTuioCursor(TuioCursor tuioCursor) {
		int x = tuioCursor.getScreenX(width);
		int y = tuioCursor.getScreenY(height);
		// log.debug("Update " + tuioCursor.getCursorID() + ": " + x + ", " + y);

		if (sketchTuioCursor != null && sketchTuioCursor.getCursorID() == tuioCursor.getCursorID()) {
			Location location = map.getLocation(x, y);
			synchronized (locations) {
				locations.add(location);
			}
		}

		tuioCursorHandler.updateTuioCursor(tuioCursor);
	}

	@Override
	public void removeTuioCursor(TuioCursor tuioCursor) {
		// log.debug("Remove " + tuioCursor.getCursorID());

		if (sketchTuioCursor != null && sketchTuioCursor.getCursorID() == tuioCursor.getCursorID()) {
			sketchTuioCursor = null;
		}

		tuioCursorHandler.removeTuioCursor(tuioCursor);
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
		// No objects used
	}

	@Override
	public void refresh(TuioTime arg0) {
		// Not used
	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
		// No objects used
	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
		// No objects used
	}

}

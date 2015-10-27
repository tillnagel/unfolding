package de.fhpotsdam.unfolding.examples.interaction.multitouch;

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

/**
 * Multitouch map with a simple button atop. A tap on the button does not affect the map.
 * 
 * Creates a map and a handler for TUIO cursors, with this application acting as TUIO listener, and forwarding the TUIO
 * events to the handler. This allows reacting to other touch interactions in the application (e.g. map markers, or
 * other interface elements), as well.
 * 
 * See simpler {@link MultitouchMapApp} if you want to use multitouch interaction for the map only.
 */
public class MultitouchMapExternalTuioApp extends PApplet implements TuioListener {

	public static Logger log = Logger.getLogger(MultitouchMapExternalTuioApp.class);

	UnfoldingMap map;
	EventDispatcher eventDispatcher;
	TuioCursorHandler tuioCursorHandler;
	TuioClient tuioClient;

	boolean activeButton = false;
	int buttonX = 50;
	int buttonY = 50;
	int buttonSize = 40;

	public static void main(String[] args) {
		String[] params = new String[] { "--present", "--bgcolor=#000000", "--hide-stop",
				"de.fhpotsdam.unfolding.examples.interaction.multitouch.MultitouchMapExternalTuioApp" };
		PApplet.main(params);
	}

	public void settings() {
		size(800, 600, P2D);
	}
	
	public void setup() {
		map = new UnfoldingMap(this);
		map.setTweening(false);
		map.zoomAndPanTo(13, new Location(1.283f, 103.833f));
		map.setPanningRestriction(new Location(1.283f, 103.833f), 30);

		eventDispatcher = new EventDispatcher();

		tuioCursorHandler = new TuioCursorHandler(this, false, map);
		eventDispatcher.addBroadcaster(tuioCursorHandler);
		eventDispatcher.register(map, "pan");
		eventDispatcher.register(map, "zoom");

		tuioClient = tuioCursorHandler.getTuioClient();
		tuioClient.addTuioListener(this);
	}

	public void draw() {
		map.draw();

		// log.debug("map.center: " + map.getCenter());

		if (activeButton) {
			fill(255, 0, 0, 150);
		} else {
			fill(255, 150);
		}
		noStroke();
		ellipse(buttonX, buttonY, buttonSize, buttonSize);

		// tuioCursorHandler.drawCursors();
		fill(255, 100);
		for (TuioCursor tcur : tuioClient.getTuioCursors()) {
			ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 20, 20);
		}
	}

	@Override
	public void addTuioCursor(TuioCursor tuioCursor) {
		int x = tuioCursor.getScreenX(width);
		int y = tuioCursor.getScreenY(height);

		log.debug("Add " + tuioCursor.getCursorID() + ": " + x + ", " + y);

		if (dist(x, y, buttonX, buttonY) < buttonSize / 2) {
			activeButton = !activeButton;
		} else {
			tuioCursorHandler.addTuioCursor(tuioCursor);
		}
	}

	@Override
	public void updateTuioCursor(TuioCursor tuioCursor) {
		int x = tuioCursor.getScreenX(width);
		int y = tuioCursor.getScreenY(height);
		log.debug("Update " + tuioCursor.getCursorID() + ": " + x + ", " + y);

		tuioCursorHandler.updateTuioCursor(tuioCursor);
	}

	@Override
	public void removeTuioCursor(TuioCursor tuioCursor) {
		log.debug("Remove " + tuioCursor.getCursorID());

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

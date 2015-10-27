package de.fhpotsdam.unfolding.examples.interaction.multitouch;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;

/**
 * An interactive map which users can zoom, pan, and rotate with finger gestures.
 * 
 * You'll need a TUIO-capable touch device to run this example! See http://www.tuio.org/?software for more information.
 * Start as application for full-screen.
 * 
 * See {@link MultitouchMapExternalTuioApp} for how to handle multitouch input for both your app and the map.
 * 
 */
public class MultitouchMapApp extends PApplet {

	public static Logger log = Logger.getLogger(MultitouchMapApp.class);

	public static final boolean DISABLE_ROTATING = false;

	public static boolean FULLSCREEN = false;

	UnfoldingMap map;
	TuioCursorHandler tuioCursorHandler;
	
	public void settings() {
		if (FULLSCREEN) {
			size(1920, 1080, P2D);
		} else {
			size(800, 600, P2D);
		}
	}
	
	public void setup() {
		// Init the map
		map = new UnfoldingMap(this);

		EventDispatcher eventDispatcher = new EventDispatcher();
		// Create multitouch input handler, and register map to listen to pan and zoom events.
		tuioCursorHandler = new TuioCursorHandler(this, map);
		eventDispatcher.addBroadcaster(tuioCursorHandler);
		eventDispatcher.register(map, "pan");
		eventDispatcher.register(map, "zoom");
	}

	public void draw() {
		background(0);

		if (DISABLE_ROTATING) {
			map.rotateTo(0);
		}
		map.draw();

		// Shows position of fingers for debugging.
		tuioCursorHandler.drawCursors();
	}

	public static void main(String[] args) {
		String[] params = new String[] { "--present", "--bgcolor=#000000", "--hide-stop",
				"de.fhpotsdam.unfolding.examples.interaction.multitouch.MultitouchMapApp" };
		FULLSCREEN = true;
		PApplet.main(params);
	}

}

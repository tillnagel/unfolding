package de.fhpotsdam.unfolding.examples.interaction;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;

public class MultitouchMapApp extends PApplet {

	public static Logger log = Logger.getLogger(MultitouchMapApp.class);

	public static final boolean DISABLE_ROTATING = false;

	// For fullscreen start as application
	public static final boolean FULLSCREEN = false;

	UnfoldingMap map;
	TuioCursorHandler tuioCursorHandler;

	public void setup() {
		if (FULLSCREEN) {
			size(1920, 1080, OPENGL);
		} else {
			size(800, 600, OPENGL);
		}

		map = new UnfoldingMap(this);

		EventDispatcher eventDispatcher = new EventDispatcher();

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

		tuioCursorHandler.drawCursors();
	}

	public static void main(String[] args) {
		String[] params = new String[] { "--present", "--bgcolor=#000000", "--hide-stop", "--exclusive",
				"de.fhpotsdam.unfolding.examples.interaction.MultitouchMapApp" };
		PApplet.main(params);
	}

}

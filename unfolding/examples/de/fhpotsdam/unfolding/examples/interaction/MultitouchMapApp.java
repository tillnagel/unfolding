package de.fhpotsdam.unfolding.examples.interaction;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;

public class MultitouchMapApp extends PApplet {

	public static Logger log = Logger.getLogger(MultitouchMapApp.class);

	public static final boolean DISABLE_ROTATING = false;

	Map map;
	EventDispatcher eventDispatcher;

	TuioCursorHandler tuioCursorHandler;

	public void setup() {
		size(400, 400, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.setTweening(false);

		eventDispatcher = new EventDispatcher();

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

}

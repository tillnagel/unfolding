package de.fhpotsdam.unfolding.examples;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;
import de.fhpotsdam.unfolding.utils.DebugDisplay;

public class MultitouchMapApp extends PApplet {

	public static Logger log = Logger.getLogger(MultitouchMapApp.class);

	Map map;
	EventDispatcher eventDispatcher;
	DebugDisplay debugDisplay;
	
	TuioCursorHandler tuioCursorHandler;

	public void setup() {
		size(1000, 750, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1", 50, 100, 900, 550);
		map.setTweening(false);
		
		eventDispatcher = new EventDispatcher();
		
		tuioCursorHandler = new TuioCursorHandler(this, map);
		eventDispatcher.addBroadcaster(tuioCursorHandler);

//		MouseHandler mouseHandler = new MouseHandler(this, map);
//		eventDispatcher.addBroadcaster(mouseHandler);

		eventDispatcher.register(map, "pan");

		debugDisplay = new DebugDisplay(this, map.mapDisplay, 0, 0, 250, 200);
	}

	public void draw() {
		background(0);

		map.draw();
		debugDisplay.draw();
		
		tuioCursorHandler.drawCursors();
	}

}

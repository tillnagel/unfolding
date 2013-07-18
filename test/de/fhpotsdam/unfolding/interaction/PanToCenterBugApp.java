package de.fhpotsdam.unfolding.interaction;

import org.apache.log4j.Logger;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Interactive test app for the current transformation bug.
 * 
 * Zoom in, set trans center (by right clicking), zoom again, set other trans center
 * 
 */
@SuppressWarnings("serial")
public class PanToCenterBugApp extends PApplet {

	public static Logger log = Logger.getLogger(PanToCenterBugApp.class);

	DebugDisplay debugDisplay;

	EventDispatcher eventDispatcher;

	UnfoldingMap map;

	// London, UK
	Location locLondon = new Location(51.50939f, 0f);
	// Quito, Equador
	Location locQuito = new Location(0f, -78f);


	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		textFont(loadFont("Miso-Light-12.vlw"));

		// Creates default mapDisplay
		map = new UnfoldingMap(this, "map", 0, 0, 800, 600);
		map.setTweening(false);

		debugDisplay = new DebugDisplay(this, map);

		// default dispatcher
		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		debugDisplay.draw();

		noFill();
		// red: London
		ScreenPosition pLondon = map.mapDisplay.getScreenPosition(locLondon);
		stroke(255, 0, 0);
		ellipse(pLondon.x, pLondon.y, 12, 12);
		
		// blue: Quito
		ScreenPosition pQuito = map.mapDisplay.getScreenPosition(locQuito);
		stroke(0, 0, 255);
		ellipse(pQuito.x, pQuito.y, 12, 12);
	}
	
	public void keyPressed() {
		if (key == '1') {
			println("panCenterTo London");
			map.panTo(locLondon);
		}
		else if (key == '2') {
			println("panCenterTo Quito");
			map.panTo(locQuito);
		}
	}
}

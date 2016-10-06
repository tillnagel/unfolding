package de.fhpotsdam.unfolding.examples.animation;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Load two maps and fade between.
 * 
 * Press key '1' to fade once, and press key '2' to start fading animation.
 */
public class FadeTwoMapsApp extends PApplet {

	private UnfoldingMap map1;
	private UnfoldingMap map2;

	// Create and set variable we need to fade between our two maps.
	private static boolean FADE_ONCE = false;
	private static boolean FADE_ALWAYS = false;
	private static int FADE_VAL = 255;
	private static int FADE_DELTA = 5;
	private static int FADE_MIN = 0;
	private static int FADE_MAX = 255;

	@Override
	public void settings() {
		size(600, 400, P2D);
	}

	@Override
	public void setup() {
		// Set the position and size of our two maps.
		int mapXposition = 0;
		int mapYposition = 30;
		int mapWidth = width;
		int mapHeight = height - mapYposition;
		// Set our location of the maps
		float lon = 52.5f;
		float lat = 13.4f;

		// Initialize two maps
		map1 = new UnfoldingMap(this, mapXposition, mapYposition, mapWidth, mapHeight);
		map1.zoomAndPanTo(10, new Location(lon, lat));
		map2 = new UnfoldingMap(this, mapXposition, mapYposition, mapWidth, mapHeight, new Microsoft.AerialProvider());
		map2.zoomAndPanTo(10, new Location(lon, lat));
		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	@Override
	public void draw() {
		background(0);

		// Calculate Fade Value
		if (FADE_ALWAYS) {
			FADE_ONCE = false;
			if (FADE_VAL == FADE_MIN || FADE_VAL == FADE_MAX)
				FADE_DELTA = -FADE_DELTA;
			FADE_VAL += FADE_DELTA;
		}

		if (FADE_ONCE) {
			if (FADE_VAL == FADE_MIN || FADE_VAL == FADE_MAX) {
				FADE_DELTA = -FADE_DELTA;
				FADE_ONCE = false;
			}
			FADE_VAL += FADE_DELTA;
		}

		// Draw maps
		tint(255);
		map1.draw();
		tint(255, FADE_VAL);
		map2.draw();

		// Description at the Top
		fill(255);
		text("Press key '1' to fade once   |   Press key '2' to fade always", 10, 20);
	}

	@Override
	public void keyPressed() {
		switch (key) {
		case '1':
			FADE_ALWAYS = false;
			FADE_ONCE = true;
			break;
		case '2':
			FADE_ALWAYS = true;
			FADE_ONCE = false;
			break;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { FadeTwoMapsApp.class.getName() });
	}
}

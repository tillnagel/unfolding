package de.fhpotsdam.unfolding.examples.image;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.LargeMapImageUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Uses LargeMapImageUtils to create a large map image, stitched together from multiple map screenshots.
 */
public class LargeMapImageSaveApp extends PApplet {

	// Set to the center location you want to grab the map for.
	Location location = new Location(52.5f, 13.4f);
	// Set to zoom level you want to grab.
	int zoomLevel = 9;

	UnfoldingMap map;

	LargeMapImageUtils lmiUtils;

	public void settings() {
		size(500, 500, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(zoomLevel, location);
		MapUtils.createDefaultEventDispatcher(this, map);

		println("Init large map image.");
		lmiUtils = new LargeMapImageUtils(this, map);
	}

	public void draw() {
		map.draw();

		lmiUtils.run();
	}

	public void keyPressed() {
		if (key == 's') {
			// Around current center and with current zoom level
			lmiUtils.init();
		}
		if (key == 'b') {
			// Around set center and zoom level (pans there before screenshoting)
			lmiUtils.init(location, zoomLevel);
		}
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { LargeMapImageSaveApp.class.getName() });
	}
}

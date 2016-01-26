package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Uses an UnfoldingMap as static image (e.g. to improve performance when no interaction needed).
 * 
 * Storing the mapImage must be done in draw(), as doing so in call-back method tilesLoaded() results in OpenGL error.
 */
public class StaticMapImageApp extends PApplet {

	UnfoldingMap map;
	PImage mapImage = null;

	public void settings() {
		size(2000, 1000, P2D);

	}

	public void setup() {
		frameRate(120);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(240);

		if (map.allTilesLoaded()) {
			if (mapImage == null) {
				mapImage = map.mapDisplay.getInnerPG().get();
			}
			image(mapImage, 0, 0);
		} else {
			map.draw();
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { StaticMapImageApp.class.getName() });
	}
}

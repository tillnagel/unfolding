package de.fhpotsdam.unfolding.examples.image;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a static image laid over an interactive background map.
 * 
 * See {@link ImageOverlayApp} for more information.
 */
public class SmallImageOverlayApp extends PApplet {

	UnfoldingMap map;
	Location center = new Location(52.396, 13.058);

	DebugDisplay debugDisplay;

	PImage visImg;
	Location visNorthWest = new Location(52.399539, 13.048003);
	Location visSouthEast = new Location(52.391667, 13.066667);

	public void settings() {
		size(1400, 800, P2D);
	}

	public void setup() {
		visImg = loadImage("shader/splendor-cutout.png");

		map = new UnfoldingMap(this, "Satellite Map", new Microsoft.AerialProvider());
		map.zoomAndPanTo(16, center);
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);
	}

	public void draw() {
		map.draw();

		ScreenPosition topRight = map.getScreenPosition(visNorthWest);
		ScreenPosition bottomLeft = map.getScreenPosition(visSouthEast);

		float width = bottomLeft.x - topRight.x;
		float height = bottomLeft.y - topRight.y;

		tint(255, 127);
		image(visImg, topRight.x, topRight.y, width, height);

		debugDisplay.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SmallImageOverlayApp.class.getName() });
	}
}

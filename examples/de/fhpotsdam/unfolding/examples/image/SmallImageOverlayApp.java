package de.fhpotsdam.unfolding.examples.image;

import processing.core.PApplet;
import processing.core.PImage;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
public class SmallImageOverlayApp extends PApplet {

	UnfoldingMap map;
	Location center = new Location(52.396, 13.058);

	DebugDisplay debugDisplay;

	PImage visImg;
	Location visNorthWest = new Location(52.399539, 13.048003);
	Location visSouthEast = new Location(52.391667, 13.066667);

	public void setup() {
		size(1400, 800, GLConstants.GLGRAPHICS);

		visImg = loadImage("splendor-cutout.png");

		map = new UnfoldingMap(this, "Satellite Map", new Microsoft.AerialProvider());
		map.zoomAndPanTo(center, 14);
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

		fill(255, 0, 0);
		// ellipse(bottomLeft.x, bottomLeft.y, 10, 10);

		debugDisplay.draw();

	}
}

package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * A loupe on a map. The loupe, a small movable map, always updates its view according to its
 * position on the large background map.
 * 
 * @author tillnagel
 */
public class MovableMapOnStaticMap extends PApplet {

	Map mapStatic;
	Map mapZoom;

	float mapZoomX = 100;
	float mapZoomY = 100;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		mapStatic = new Map(this, "static", 0, 0, 800, 600);
		mapZoom = new Map(this, "zoom", 400, 300, 150, 150);
		mapZoom.setTweening(false);
		mapZoom.zoomToLevel(6);
	}

	public void draw() {
		background(0);

		mapStatic.draw();
		mapZoom.draw();

		noFill();
		strokeWeight(5);
		strokeJoin(MITER);
		stroke(40, 50);
		rect(mapZoomX, mapZoomY, 150, 150);
	}

	public void mouseMoved() {
		// Move the small map to mouse position, but center it around it
		mapZoomX = mouseX - mapZoom.mapDisplay.getWidth() / 2;
		mapZoomY = mouseY - mapZoom.mapDisplay.getHeight() / 2;
		mapZoom.move(mapZoomX, mapZoomY);

		// Read geo location of the mouse position from the background map
		Location locationOnStaticMap = mapStatic.mapDisplay.getLocationFromScreenPosition(mouseX,
				mouseY);
		// Pan the small map toward that location
		mapZoom.panTo(locationOnStaticMap);
	}

}

package de.fhpotsdam.unfolding.examples.multi.overlay;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * A loupe on a map. The loupe, a small movable map, always updates its view according to its position on the large
 * background map.
 */
public class MovableMapOnStaticMapApp extends PApplet {

	UnfoldingMap mapStatic;
	UnfoldingMap mapZoom;

	float mapZoomX = 100;
	float mapZoomY = 100;

	public void settings() {
		size(750, 600, P2D);
	}

	public void setup() {
		mapStatic = new UnfoldingMap(this, "static", 0, 0, 750, 600);
		mapZoom = new UnfoldingMap(this, "zoom", 400, 300, 150, 150);
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
		Location locationOnStaticMap = mapStatic.getLocation(mouseX, mouseY);
		// Pan the small map toward that location
		mapZoom.panTo(locationOnStaticMap);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MovableMapOnStaticMapApp.class.getName() });
	}

}

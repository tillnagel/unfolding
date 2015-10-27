package de.fhpotsdam.unfolding.examples.multi.overlay;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * A small map is laid atop the interactive background map. The overlay map shows the satellite view, while the
 * background map shows a simplified map. Interaction is reflected in both maps, so the overlay map acts as an
 * interactive window to another map layer.
 * 
 * Demonstrates how to position a second map according to its position on the background map.
 */
public class SatelliteOverlayApp extends PApplet {

	UnfoldingMap mapOverview;
	UnfoldingMap mapOverlay;

	float mapZoomX = 100;
	float mapZoomY = 100;

	public void settings() {
		size(750, 600, P2D);
	}

	public void setup() {
		mapOverview = new UnfoldingMap(this, "static", 0, 0, 750, 600);
		mapOverview.zoomToLevel(2);
		mapOverlay = new UnfoldingMap(this, "zoom", 400, 300, 150, 150, true, false, new Microsoft.AerialProvider());
		mapOverlay.zoomToLevel(2);

		MapUtils.createDefaultEventDispatcher(this, mapOverview, mapOverlay);
	}

	public void draw() {
		background(0);

		mapOverview.draw();
		mapOverlay.draw();

		noFill();
		strokeWeight(5);
		strokeJoin(MITER);
		stroke(40, 50);
		rect(mapZoomX, mapZoomY, 150, 150);
	}

	public void mouseDragged() {
		moveOverlay(mouseX, mouseY);
	}

	public void mouseMoved() {
		moveOverlay(mouseX, mouseY);
	}

	private void moveOverlay(int x, int y) {
		// Move the small map to mouse position, but center it around it
		mapZoomX = x - mapOverlay.mapDisplay.getWidth() / 2;
		mapZoomY = y - mapOverlay.mapDisplay.getHeight() / 2;
		mapOverlay.move(mapZoomX, mapZoomY);

		// Read geo location of the mouse position from the background map
		Location locationOnOverviewMap = mapOverview.getLocation(x, y);
		// Pan the small map toward that location
		mapOverlay.panTo(locationOnOverviewMap);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SatelliteOverlayApp.class.getName() });
	}

}

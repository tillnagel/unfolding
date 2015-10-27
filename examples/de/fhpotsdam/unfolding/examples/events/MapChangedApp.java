package de.fhpotsdam.unfolding.examples.events;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple way of reacting to map events in an application.
 * 
 * Listen to events in the mapChanged() method. In this example, a simple zoom animation is shown.
 */
public class MapChangedApp extends PApplet {

	UnfoldingMap map;
	Location lastZoomLocation = null;
	float rectSize = 50;
	float rectSizeDiff;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MapChangedApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.setTweening(true);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();

		if (lastZoomLocation != null) {
			// Animates a rectangle to indicate where the zoom happened
			ScreenPosition pos = map.getScreenPosition(lastZoomLocation);
			noFill();
			stroke(255, 0, 0, 200);
			rectMode(CENTER);
			rect(pos.x, pos.y, rectSize, rectSize);
			rectSize += rectSizeDiff;
			if (rectSize < 10 || rectSize > 50) {
				lastZoomLocation = null;
			}
		}
	}

	public void mapChanged(MapEvent mapEvent) {
		if (mapEvent.getType().equals(ZoomMapEvent.TYPE_ZOOM)) {
			// Reacts to zoom events and reads location and zoom value.
			ZoomMapEvent zoomMapEvent = (ZoomMapEvent) mapEvent;
			lastZoomLocation = zoomMapEvent.getCenter();
			if (zoomMapEvent.getZoomLevelDelta() < 0 || zoomMapEvent.getZoomDelta() < 0) {
				// Zoom in
				rectSize = 50;
				rectSizeDiff = -3;
			} else {
				// Zoom out
				rectSize = 10;
				rectSizeDiff = 3;
			}
		}
	}

}
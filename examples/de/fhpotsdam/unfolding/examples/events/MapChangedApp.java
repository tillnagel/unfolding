package de.fhpotsdam.unfolding.examples.events;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Simple way of reacting to map events in an application.
 */
public class MapChangedApp extends PApplet {

	UnfoldingMap map;
	Location lastZoomLocation = null;
	float rectSize = 50;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
		if (lastZoomLocation != null) {
			ScreenPosition pos = map.getScreenPosition(lastZoomLocation);
			noFill();
			stroke(255, 0, 0, 200);
			rectMode(CENTER);
			rect(pos.x, pos.y, rectSize, rectSize);
			rectSize -= 2f;
			if (rectSize < 2) {
				lastZoomLocation = null;
			}
		}
	}

	public void mapChanged(MapEvent mapEvent) {
		if (mapEvent.getType().equals(ZoomMapEvent.TYPE_ZOOM)) {
			ZoomMapEvent zoomMapEvent = (ZoomMapEvent) mapEvent;
			lastZoomLocation = zoomMapEvent.getCenter();
			rectSize = 50;
		}
	}

}
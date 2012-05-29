package de.fhpotsdam.unfolding.examples.events;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple way of reacting to map events in an app. Without the need to implement MapEventListener.
 * 
 * TODO Does not work, yet. Implement getMethod lookup and invoking mapChanged in Map.
 * 
 */
public class SimpleMapListenerApp extends PApplet {

	Map map;
	Location oldLocation = new Location(0, 0);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
	}

	public void mapChanged(MapEvent mapEvent) {
		if (mapEvent.getType().equals(PanMapEvent.TYPE_PAN)) {
			Location location = map.getCenter();
			double dist = GeoUtils.getDistance(oldLocation, location);
			println("Panned by " + dist + " km");
			oldLocation = location;
		}
	}

}
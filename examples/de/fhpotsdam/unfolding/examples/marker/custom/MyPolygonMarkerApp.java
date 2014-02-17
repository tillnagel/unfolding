package de.fhpotsdam.unfolding.examples.marker.custom;

import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example showing how to create and use your own custom PolygonMarker.
 */
public class MyPolygonMarkerApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomTo(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		MyPolygonMarker polygonMarker = new MyPolygonMarker();
		// Either add single location with lat, lng
		polygonMarker.addLocation(15, 5);
		// Or add multiple locations
		polygonMarker.addLocations(new Location(22, 12), new Location(23, 7));
		map.addMarkers(polygonMarker);
	}

	public void draw() {
		map.draw();
	}

	// Very simple custom PolygonMarker. Extends Unfolding's SimplePolygonMarker to create own drawing methods.
	class MyPolygonMarker extends SimplePolygonMarker {

		public void draw(PGraphics pg, List<MapPosition> mapPositions) {
			pg.pushStyle();

			// Here you should do your custom drawing
			pg.strokeWeight(2);
			pg.stroke(255, 255, 0);
			pg.fill(255, 0, 0, 127);
			pg.beginShape();
			for (MapPosition mapPosition : mapPositions) {
				pg.vertex(mapPosition.x, mapPosition.y);
			}
			pg.endShape();

			pg.popStyle();
		}

	}
}

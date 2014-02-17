package de.fhpotsdam.unfolding.examples.marker.custom;

import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example showing how to create and use your own custom LinesMarker.
 */
public class MyLinesMarkerApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomTo(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		MyLinesMarker lineMarker = new MyLinesMarker(new Location(10, 10), new Location(12, 20));
		map.addMarkers(lineMarker);
	}

	public void draw() {
		map.draw();
	}

	// Very simple custom LinesMarker. Extends Unfolding's SimpleLinesMarker to create own drawing methods.
	public class MyLinesMarker extends SimpleLinesMarker {

		public MyLinesMarker(Location start, Location end) {
			super(start, end);
		}

		public void draw(PGraphics pg, List<MapPosition> mapPositions) {
			pg.pushStyle();

			// Here you should do your custom drawing
			pg.strokeWeight(3);
			pg.stroke(255, 0, 0);
			pg.beginShape();
			for (MapPosition mapPosition : mapPositions) {
				pg.vertex(mapPosition.x, mapPosition.y);
			}
			pg.endShape();

			pg.popStyle();
		}

	}
}

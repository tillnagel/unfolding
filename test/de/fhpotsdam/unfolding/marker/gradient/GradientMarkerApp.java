package de.fhpotsdam.unfolding.marker.gradient;

import java.util.ArrayList;
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
public class GradientMarkerApp extends PApplet {

	UnfoldingMap map;
	MyPolygonMarker polygonMarker = new MyPolygonMarker();

	public void setup() {
		size(800, 600, P2D);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(4);
		MapUtils.createDefaultEventDispatcher(this, map);

		polygonMarker.addLocations(new Location(6.403, -17.139), new Location(16.805, -6.768), new Location(16.973,
				12.217), new Location(5.091, 20.127), new Location(-13.154, 15.117), new Location(-11.437, -10.635));
		polygonMarker.initColors();
		map.addMarkers(polygonMarker);
	}

	public void draw() {
		map.draw();
	}

	// Very simple custom PolygonMarker. Extends Unfolding's SimplePolygonMarker to create own drawing methods.
	class MyPolygonMarker extends SimplePolygonMarker {

		List<Integer> colors = new ArrayList<Integer>();
		
		public void initColors() {
			colors.clear();
			for (int i = 0; i < locations.size(); i++) {
				colors.add(color(random(255), random(255), random(255)));
			}
		}

		public void draw(PGraphics pg, List<MapPosition> mapPositions) {
			pg.pushStyle();

			pg.strokeWeight(2);
			pg.stroke(0, 200);
			pg.beginShape();
			int i = 0;
			for (MapPosition mapPosition : mapPositions) {
				pg.fill(colors.get(i), 100);
				pg.vertex(mapPosition.x, mapPosition.y);
				i++;
			}
			pg.endShape(CLOSE);

			pg.popStyle();
		}

	}
}

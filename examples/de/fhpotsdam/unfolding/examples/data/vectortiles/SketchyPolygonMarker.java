package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import org.gicentre.handy.HandyRenderer;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SketchyPolygonMarker extends SimplePolygonMarker {

	HandyRenderer handy = null;

	public SketchyPolygonMarker(List<Location> locations) {
		super(locations);
	}

	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		handy.setGraphics(pg);

		pg.pushStyle();

		// Here you should do your custom drawing
		pg.strokeWeight(2);
		pg.stroke(209, 89, 237);
		pg.fill(180, 127);

		handy.beginShape();
		for (MapPosition pos : mapPositions) {
			handy.vertex(pos.x, pos.y);
		}
		handy.endShape();

		pg.popStyle();
	}
	
	public void draw(PGraphics pg, List<MapPosition> mapPositions, List<List<MapPosition>> ringMapPositionsArray) {

		pg.pushStyle();

		// Here you should do your custom drawing
		pg.strokeWeight(2);
		pg.stroke(209, 89, 237);
		pg.fill(180, 127);

		handy.beginShape();

		// Main shape (outline)
		for (MapPosition pos : mapPositions) {
			handy.vertex(pos.x, pos.y);
		}

		// Interior rings
//		for (List<MapPosition> interiorRing : ringMapPositionsArray) {
//			handy.beginContour();
//			for (MapPosition pos : interiorRing) {
//				handy.vertex(pos.x, pos.y);
//			}
//			handy.endContour();
//		}

		handy.endShape();

		pg.popStyle();
	}

	public void setHandyRenderer(HandyRenderer handy) {
		this.handy = handy;
	}
}
package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

/**
 * Marker representing multiple locations as polygon. Use directly to display as simple colored polygon, or extend it
 * for custom styles.
 * 
 * This polygon marker handles interior rings, i.e. holes in the main shape. When in need of multiple connected polygons
 * (e.g. a country and an island) use MultiMarker with Polygons.
 */
public class SimplePolygonMarker extends AbstractShapeMarker {

	public SimplePolygonMarker() {
		super();
	}

	/**
	 * Creates a polygon marker.
	 * 
	 * @param locations
	 *            The locations to display as polygon.
	 */
	public SimplePolygonMarker(List<Location> locations) {
		super(locations);
	}

	/**
	 * Creates a polygon marker with additional properties.
	 * 
	 * @param locations
	 *            The locations to display as polygon.
	 * @param properties
	 *            Optional data properties.
	 */
	public SimplePolygonMarker(List<Location> locations, HashMap<String, Object> properties) {
		super(locations, properties);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		if (mapPositions.isEmpty() || isHidden())
			return;

		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		if (isSelected()) {
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
		} else {
			pg.fill(color);
			pg.stroke(strokeColor);
		}

		pg.beginShape();
		for (MapPosition pos : mapPositions) {
			pg.vertex(pos.x, pos.y);
		}
		pg.endShape(PConstants.CLOSE);
		pg.popStyle();
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions, List<List<MapPosition>> ringMapPositionsArray) {
		if (mapPositions.isEmpty() || isHidden())
			return;

		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		if (isSelected()) {
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
		} else {
			pg.fill(color);
			pg.stroke(strokeColor);
		}

		pg.beginShape();

		// Main shape (outline)
		for (MapPosition pos : mapPositions) {
			pg.vertex(pos.x, pos.y);
		}

		// Interior rings
		for (List<MapPosition> interiorRing : ringMapPositionsArray) {
			pg.beginContour();
			for (MapPosition pos : interiorRing) {
				pg.vertex(pos.x, pos.y);
			}
			pg.endContour();
		}

		pg.endShape(PConstants.CLOSE);
		pg.popStyle();
	}

}

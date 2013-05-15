package de.fhpotsdam.unfolding.examples.marker.advanced.centroid;

import java.util.HashMap;
import java.util.List;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;

/**
 * Displays the polygon ID as label at the geometric center of its shape. For this, it uses the geo-spatial centroid of
 * the polygon, and converts it to object coordinates, to be consistent to other marker draw methods.
 */
public class CentroidLabelMarker extends SimplePolygonMarker {

	public CentroidLabelMarker(List<Location> locations) {
		super(locations);
	}

	// Overrides the method with map, as we need to convert the centroid location ourself.
	@Override
	protected void draw(PGraphics pg, List<MapPosition> mapPositions, HashMap<String, Object> properties,
			UnfoldingMap map) {

		// Polygon shape is drawn by the SimplePolygonMarker
		super.draw(pg, mapPositions, properties, map);

		// Draws the country code at the centroid of the polygon
		if (getId() != null) {
			pg.pushStyle();

			// Gets geometric center as geo-location
			Location centerLocation = getCentroid();
			// Converts geo-location to position on the map (NB: Not the screen!)
			float[] xy = map.mapDisplay.getObjectFromLocation(centerLocation);
			int x = Math.round(xy[0] - pg.textWidth(getId()) / 2);
			int y = Math.round(xy[1] + 6);

			// Draws label
			pg.fill(255);
			pg.text(getId(), x, y);
			pg.popStyle();
		}
	}
}

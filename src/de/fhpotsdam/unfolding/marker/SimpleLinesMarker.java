package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

/**
 * Marker representing multiple locations as lines. Use directly to display as simple lines, or extend it for custom
 * styles.
 * 
 * This can be a polyline consisting of multiple locations, or a single line consisting of two locations.
 */
public class SimpleLinesMarker extends AbstractShapeMarker {

	public SimpleLinesMarker() {
		super();
	}

	/**
	 * Creates a polyline marker.
	 * 
	 * @param locations
	 *            The locations to connect via lines.
	 */
	public SimpleLinesMarker(List<Location> locations) {
		super(locations);
	}

	/**
	 * Creates a polyline marker with additional properties.
	 * 
	 * @param locations
	 *            The locations to connect via lines.
	 * @param properties
	 *            Optional data properties.
	 */
	public SimpleLinesMarker(List<Location> locations, HashMap<String, Object> properties) {
		super(locations, properties);
	}

	/**
	 * Creates a marker for a single line, with a connection from start to end location. This convenience method adds
	 * the given start and end locations to the list.
	 * 
	 * @param startLocation
	 *            The location of the start of this line.
	 * @param endLocation
	 *            The location of the end of this line.
	 */
	public SimpleLinesMarker(Location startLocation, Location endLocation) {
		addLocations(startLocation, endLocation);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		if (mapPositions.isEmpty() || isHidden())
			return;

		pg.pushStyle();
		pg.noFill();
		if (isSelected()) {
			pg.stroke(highlightColor);
		} else {
			pg.stroke(color);
		}
		pg.strokeWeight(strokeWeight);
		pg.smooth();

		pg.beginShape(PConstants.LINES);
		MapPosition last = mapPositions.get(0);
		for (int i = 1; i < mapPositions.size(); ++i) {
			MapPosition mp = mapPositions.get(i);
			pg.vertex(last.x, last.y);
			pg.vertex(mp.x, mp.y);

			last = mp;
		}
		pg.endShape();
		pg.popStyle();
	}

}

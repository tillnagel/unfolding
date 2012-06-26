package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SimpleLinesMarker extends AbstractShapeMarker {

	public SimpleLinesMarker() {
		super();
	}

	public SimpleLinesMarker(List<Location> locations) {
		super(locations);
	}

	public SimpleLinesMarker(List<Location> locations, HashMap<String, Object> properties) {
		super(locations, properties);
	}

	/**
	 * Convenient method to create a marker with a single line.
	 * 
	 * @param startLocation
	 * @param endLocation
	 */
	public SimpleLinesMarker(Location startLocation, Location endLocation) {
		addLocations(startLocation, endLocation);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
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

	@Override
	public void drawOuter(PGraphics pg, List<MapPosition> mapPositions) {
	}

}

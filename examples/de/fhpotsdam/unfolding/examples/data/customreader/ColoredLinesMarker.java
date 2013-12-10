package de.fhpotsdam.unfolding.examples.data.customreader;

import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;

/**
 * Custom marker to show colored lines, depending on speed values from custom data reader.
 */
public class ColoredLinesMarker extends AbstractShapeMarker {

	public ColoredLinesMarker(List<Location> locations, HashMap<String, Object> properties) {
		super(locations);
		setProperties(properties);
	}

	@Override
	protected void draw(PGraphics pg, List<MapPosition> mapPositions, HashMap<String, Object> properties,
			UnfoldingMap map) {
		pg.pushStyle();

		@SuppressWarnings("unchecked")
		List<Double> speedList = (List<Double>) properties.get("speedList");

		MapPosition oldPos = null;
		for (int i = 0; i < mapPositions.size(); i++) {
			MapPosition pos = mapPositions.get(i);
			if (i > 0) {				
				// Draw a line
				pg.strokeWeight(4);
				// Map speed to color of line
				pg.stroke(255 - PApplet.map(speedList.get(i).floatValue(), 0, 30, 0, 255),
						PApplet.map(speedList.get(i).floatValue(), 0, 30, 0, 255), 0, 200);
				pg.line(oldPos.x, oldPos.y, pos.x, pos.y);
			}
			oldPos = pos;
			i++;
		}

		pg.popStyle();
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> objectPositions) {
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

}

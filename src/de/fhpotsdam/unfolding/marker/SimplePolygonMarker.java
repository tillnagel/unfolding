package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SimplePolygonMarker extends AbstractShapeMarker {

	public SimplePolygonMarker() {
		super();
	}

	public SimplePolygonMarker(List<Location> locations) {
		super(locations);
	}

	public SimplePolygonMarker(List<Location> locations, HashMap<String, Object> properties) {
		super(locations, properties);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		// TODO Draw polygon marker in draw() or in drawOuter()? See https://github.com/tillnagel/unfolding/issues/25

		pg.pushStyle();
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

	public void setColor(int color) {
		this.color = color;
	}

	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
	}

	public void setHighlightStrokeColor(int highlightStrokeColor) {
		this.highlightStrokeColor = highlightStrokeColor;
	}

}

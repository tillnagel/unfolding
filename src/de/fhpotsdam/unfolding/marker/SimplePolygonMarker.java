package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.StyleConstants;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SimplePolygonMarker extends AbstractShapeMarker {

	protected int color = StyleConstants.DEFAULT_FILL_COLOR;
	protected int strokeColor = StyleConstants.DEFAULT_STROKE_COLOR;
	protected int highlightColor = StyleConstants.HIGHLIGHTED_FILL_COLOR;
	protected int highlightStrokeColor = StyleConstants.HIGHLIGHTED_STROKE_COLOR;

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
		// REVISIT move to abstractMarker.draw(map)?
		pg.pushStyle();
		if (isSelected()) {
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
		} else {
			pg.fill(color);
			pg.stroke(strokeColor);
		}
		pg.beginShape();
		for (MapPosition op : mapPositions) {
			pg.vertex(op.x, op.y);
		}
		pg.endShape(PConstants.CLOSE);
		pg.popStyle();
	}

	@Override
	public void drawOuter(PGraphics pg, List<MapPosition> mapPositions) {
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

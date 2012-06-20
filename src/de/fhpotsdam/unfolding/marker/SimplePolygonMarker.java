package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SimplePolygonMarker extends AbstractMultiMarker {

	protected int color = 1684300900;
	protected int strokeColor;

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
		pg.fill(color);
		pg.stroke(strokeColor);
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

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

}

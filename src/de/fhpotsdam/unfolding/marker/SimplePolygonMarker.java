package de.fhpotsdam.unfolding.marker;

import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SimplePolygonMarker extends AbstractMultiMarker {

	public SimplePolygonMarker() {
		super();
	}

	public SimplePolygonMarker(List<Location> locations) {
		super(locations);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		// REVISIT move to abstractMarker.draw(map)?
		pg.pushStyle();
		pg.fill(100, 90, 240, 100);
		pg.stroke(50, 50, 50, 200);
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

}

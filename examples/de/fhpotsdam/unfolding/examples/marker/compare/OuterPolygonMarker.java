package de.fhpotsdam.unfolding.examples.marker.compare;

import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class OuterPolygonMarker extends AbstractShapeMarker {


	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
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

}

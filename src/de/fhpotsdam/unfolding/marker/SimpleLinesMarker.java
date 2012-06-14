package de.fhpotsdam.unfolding.marker;

import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class SimpleLinesMarker extends AbstractMultiMarker {

	public SimpleLinesMarker() {
		super();
	}

	public SimpleLinesMarker(List<Location> locations) {
		super(locations);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		pg.pushStyle();
		pg.fill(100, 90, 240, 100);
		pg.stroke(50, 50, 50, 200);

		pg.beginShape(PConstants.LINES);
		// TODO this way its equal to LINE_STRIP but LINE_STRIP doesnt work
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

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

}

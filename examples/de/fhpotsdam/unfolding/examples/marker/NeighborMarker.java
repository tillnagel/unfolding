package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

public class NeighborMarker extends AbstractMarker {

	public Location location;

	public Location getLocation() {
		return location;
	}

	public void draw(PGraphics pg, float x, float y) {
	}

	public void drawOuter(PGraphics pg, float x, float y) {
		pg.fill(203, 79, 91, 200);
		pg.stroke(0, 50);
		pg.ellipse(x, y, 10, 10);
	}

	public boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}
}

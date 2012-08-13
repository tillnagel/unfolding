package de.fhpotsdam.tests.marker.compare;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * Example for implementing and designing your own Marker (by extending the AbstractMarker). Simple marker, implementing
 * the drawOuter method instead of the main draw method. The given x,y coordinates are already converted into the local
 * coordinate system, so no need for further conversion.
 */
public class OuterPointMarker extends AbstractMarker {

	private int radius;

	public OuterPointMarker(Location location) {
		super(location);
	}

	public void drawIn(PGraphics pg, float x, float y) {
	}

	public void draw(PGraphics pg, float x, float y) {
		pg.fill(203, 79, 91, 200);
		pg.stroke(0, 50);
		pg.ellipse(x, y, radius, radius);
	}

	public boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}

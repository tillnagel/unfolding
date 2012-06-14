package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * Example for implementing and designing your own Marker (by extending the AbstractMarker). Simple marker, implementing
 * the drawOuter method instead of the main draw method. The given x,y coordinates are already converted into the local
 * coordinate system, so no need for further conversion.
 */
public class DrawOuterMarker extends AbstractMarker {

	public DrawOuterMarker(Location location) {
		super(location);
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

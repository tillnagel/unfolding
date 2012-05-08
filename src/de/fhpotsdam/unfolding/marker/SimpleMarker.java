package de.fhpotsdam.unfolding.marker;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * Simple marker, implementing only the main draw method. The given x,y coordinates are already
 * converted into the local coordinate system, so no need for further conversion.
 */

public class SimpleMarker extends AbstractMarker {

	public void draw(PGraphics pg, float x, float y) {
		pg.fill(203, 79, 91, 200);
		pg.stroke(0, 50);
		pg.ellipse(x, y, 1, 1);
	}

	public void drawOuter(PGraphics pg, float x, float y) {
		
	}

	public boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

}

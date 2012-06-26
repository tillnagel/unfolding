package de.fhpotsdam.unfolding.examples.marker.connectionmarker;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class DotMarker extends SimplePointMarker {

	public DotMarker(Location location) {
		super(location);
		radius = 10;
	}

	public void draw(PGraphics pg, float x, float y) {
	}

	public void drawOuter(PGraphics pg, float x, float y) {
		pg.fill(color);
		pg.stroke(strokeColor);
		pg.ellipse(x, y, radius, radius);
	}

}

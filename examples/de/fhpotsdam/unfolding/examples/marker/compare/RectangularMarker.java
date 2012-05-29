package de.fhpotsdam.unfolding.examples.marker.compare;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;

public class RectangularMarker extends SimpleMarker {

	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(r, g, b, a);
		pg.stroke(0, 50);
		pg.rectMode(PApplet.CENTER);
		pg.rect(x, y, radius, radius);
		pg.popStyle();
	}

}

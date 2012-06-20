package de.fhpotsdam.unfolding.examples.marker.compare;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleMarker;

public class InnerLabelMarker extends SimpleMarker {

	public InnerLabelMarker(Location location) {
		super(location);
	}

	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(color);
		pg.stroke(strokeColor);
		pg.rectMode(PApplet.CENTER);
		pg.rect(x, y, 10, 5);
		pg.popStyle();
	}

}

package de.fhpotsdam.unfolding.examples.marker.compare;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class InnerLabelMarker extends SimplePointMarker {

	public InnerLabelMarker(Location location) {
		super(location);
	}

	public void drawIn(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(color);
		pg.stroke(strokeColor);
		pg.rectMode(PApplet.CENTER);
		pg.rect(x, y, 10, 5);
		pg.popStyle();
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
	}

}

package de.fhpotsdam.unfolding.examples.marker.compare;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleMarker;

public class OuterLabelMarker extends SimpleMarker {
	
	public OuterLabelMarker(Location location) {
		super(location);
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
	}

	@Override
	public void drawOuter(PGraphics pg, float x, float y) {
		pg.pushStyle(); 
		pg.fill(r, g, b, a);
		pg.stroke(0, 50);
		pg.rectMode(PApplet.CENTER);
		pg.rect(x, y, 50, 20);
		pg.popStyle();
	}

}

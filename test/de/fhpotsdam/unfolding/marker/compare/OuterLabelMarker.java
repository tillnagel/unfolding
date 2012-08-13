package de.fhpotsdam.unfolding.marker.compare;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class OuterLabelMarker extends SimplePointMarker {
	
	public OuterLabelMarker(Location location) {
		super(location);
	}

	@Override
	public void drawIn(PGraphics pg, float x, float y) {
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle(); 
		pg.fill(color);
		pg.stroke(strokeColor);
		pg.rectMode(PApplet.CENTER);
		pg.rect(x, y, 50, 20);
		pg.popStyle();
	}

}

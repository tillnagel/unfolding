package de.fhpotsdam.unfolding.rendering;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class TestMarker extends SimplePointMarker {

	public TestMarker(Location location) {
		super(location);
		diameter = 20;
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		
		int xi = Math.round(x);
		int yi = Math.round(y);

		PApplet.println("m: " + x + ", " + y);
		PApplet.println("m: " + xi + ", " + yi);

		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		pg.fill(color);
		pg.stroke(strokeColor);
		pg.ellipse(xi, yi, diameter, diameter);

		pg.fill(0);
		pg.text("Xylophon", xi + 20, yi);

		pg.popStyle();
	}

}

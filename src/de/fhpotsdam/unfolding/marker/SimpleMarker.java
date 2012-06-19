package de.fhpotsdam.unfolding.marker;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Simple marker, implementing only the main draw method. The given x,y coordinates are already converted into the local
 * coordinate system, so no need for further conversion.
 */

public class SimpleMarker extends AbstractMarker {

	public float radius;
	public float r, g, b, a;

	public SimpleMarker() {
		this(null);
	}
	
	public SimpleMarker(Location location) {
		super(location);
		
		radius = 1.f;
		r = 203;
		g = 79;
		b = 91;
		a = 200;
	}

	public void draw(PGraphics pg, float x, float y) {
		pg.fill(r, g, b, a);
		pg.stroke(0, 50);
		pg.ellipse(x, y, radius, radius);// TODO use radius in km and convert to px
	}

	public void drawOuter(PGraphics pg, float x, float y) {

	}

	public boolean isInside(float checkX, float checkY, float x, float y) {
		PVector pos = new PVector(x, y);
		return pos.dist(new PVector(checkX, checkY)) < radius; // FIXME must be zoom dependent
	}

	public void setColor(float _r, float _g, float _b, float _a) {
		r = _r;
		g = _g;
		b = _b;
		a = _a;
	}

	public void setRadius(float _radius) {
		radius = _radius;
	}

}

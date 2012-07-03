package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Simple marker, implementing only the main draw method.
 */

public class SimplePointMarker extends AbstractMarker {

	public float radius = 10f;

	public SimplePointMarker() {
		this(null, null);
	}

	public SimplePointMarker(Location location) {
		this(location, null);
	}

	public SimplePointMarker(Location location, HashMap<String, Object> properties) {
		super(location, properties);
	}

	public void draw(PGraphics pg, float x, float y) {
	}

	public void drawOuter(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		if (isSelected()) {
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
		} else {
			pg.fill(color);
			pg.stroke(strokeColor);
		}
		pg.ellipse(x, y, radius, radius);// TODO use radius in km and convert to px
		pg.popStyle();
	}

	public boolean isInside(float checkX, float checkY, float x, float y) {
		PApplet.println("SimpleMarker.isInside(cx, cy, x, y)");
		PVector pos = new PVector(x, y);
		return pos.dist(new PVector(checkX, checkY)) < radius; // FIXME must be zoom dependent
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

}

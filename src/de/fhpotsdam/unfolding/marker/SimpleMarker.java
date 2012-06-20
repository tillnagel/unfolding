package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.StyleConstants;

/**
 * Simple marker, implementing only the main draw method.
 */

public class SimpleMarker extends AbstractMarker {

	protected int color = StyleConstants.DEFAULT_FILL_COLOR;
	protected int strokeColor = StyleConstants.DEFAULT_STROKE_COLOR;
	protected int highlightColor = StyleConstants.HIGHLIGHTED_FILL_COLOR;
	protected int highlightStrokeColor = StyleConstants.HIGHLIGHTED_STROKE_COLOR;

	public float radius = 1f;

	public SimpleMarker() {
		this(null, null);
	}

	public SimpleMarker(Location location) {
		this(location, null);
	}

	public SimpleMarker(Location location, HashMap<String, Object> properties) {
		super(location, properties);
	}

	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
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

	public void drawOuter(PGraphics pg, float x, float y) {

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

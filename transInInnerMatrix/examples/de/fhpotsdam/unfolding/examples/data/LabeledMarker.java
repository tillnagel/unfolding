package de.fhpotsdam.unfolding.examples.data;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Marker to show a data point on a plane.
 * 
 * @author tillnagel
 */
public class LabeledMarker implements Marker {

	public String name;

	protected Location location;
	protected float x;
	protected float y;
	protected float size;

	public int color = 0;
	public int highlightColor = -256;
	
	boolean selected = false;

	PFont font;
	PApplet p;

	public LabeledMarker(PApplet p, Location location, float size) {
		this.p = p;
		this.location = location;
		this.size = size;
	}

	public LabeledMarker(PApplet p, PFont font, String name, Location location, float size) {
		this(p, location, size);
		this.name = name;
		this.font = font;
	}

	/**
	 * Two usage/implementation options: - For single map usage, use getScreenPosForLocation and
	 * draw them in main app (on top of the map) - For other usage, use MarkerManager with
	 * getObjectPosForLocation
	 */

	@Override
	public void update(PVector v) {
		x = v.x;
		y = v.y;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	/**
	 * Draws this marker, if {@link #isVisible()}. Calls {@link #drawHighlight(float, float)} if
	 * {@link #isHighlighted(float, float)}. All these methods can be overridden in sub-classes for
	 * own implementations.
	 */
	public void draw(PGraphics pg) {
		if (!isVisible()) {
			return;
		}

		if (isHighlighted()) {
			pg.fill(highlightColor, 100);
		} else {
			pg.fill(color, 30);
		}

		pg.stroke(color, 10);
		pg.strokeWeight(1);
		pg.ellipse(x, y, size, size);
		pg.strokeWeight(2);
		pg.stroke(color, 100);
		pg.point(x, y);

		if (selected) {
			drawLabel(pg);
		}
	}

	public void draw() {
		draw(p.g);
	}

	/**
	 * Displays this marker's name in a box.
	 */
	protected void drawLabel(PGraphics pg) {
		if (name != null) {
			pg.textFont(font);
			pg.fill(color, 200);
			pg.noStroke();
			pg.rect(x + 1, y - 15, pg.textWidth(name) + 2, 12);
			pg.fill(highlightColor, 200);
			pg.text(name, x + 2, y - 5);
		}
	}

	/**
	 * Checks whether the given position is in close proximity to this Marker. Used e.g. for
	 * indicating whether this Marker is selected.
	 */
	public boolean isOver(float checkX, float checkY) {
		selected = PApplet.dist(checkX, checkY, x, y) < size / 2;
		return selected;
	}

	/**
	 * Indicates whether this marker is highlighted.
	 */
	public boolean isHighlighted() {
		return false;
	}

	/**
	 * Indicates whether this marker is visible, and shall be drawn.
	 * 
	 * @return true if visible, false otherwise.
	 */
	public boolean isVisible() {
		return true;
	}

}

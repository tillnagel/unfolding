package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * Marker to show a data point on the map as sized circle. This marker has different states, i.e. visible and selected.
 * It shows a label, if selected.
 * 
 * This marker is slightly more complex, as it handles 
 * 
 */
public class LabeledMarker extends AbstractMarker {

	public String name;
	protected Location location;
	protected float size;

	public int color = 0;
	public int highlightColor = -256;

	protected boolean selected = false;
	protected boolean visible = true;

	private PFont font;

	public LabeledMarker(Location location, float size) {
		this.location = location;
		this.size = size;
	}

	public LabeledMarker(PFont font, String name, Location location, float size) {
		this(location, size);
		this.name = name;
		this.font = font;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	public void draw(PGraphics pg, float x, float y) {
		if (!isVisible()) {
			return;
		}

		pg.fill(color, 30);
		pg.stroke(color, 10);
		pg.strokeWeight(1);
		pg.ellipse(x, y, size, size);
		pg.strokeWeight(2);
		pg.stroke(color, 100);
		pg.point(x, y);
	}

	/**
	 * Displays this marker's name in a box.
	 */
	public void drawOuter(PGraphics pg, float x, float y) {
		if (selected && name != null) {
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
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		// FIXME Marker's size is not scaled according to map transformation.
		return PApplet.dist(checkX, checkY, x, y) < size / 2;
	}

	/**
	 * Indicates whether this marker is visible, and shall be drawn.
	 * 
	 * @return true if visible, false otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

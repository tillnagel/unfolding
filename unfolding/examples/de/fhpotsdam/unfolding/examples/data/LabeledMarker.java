package de.fhpotsdam.unfolding.examples.data;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.Map;
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

	protected boolean selected = false;

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

	public void draw(Map map) {
		PGraphics pg = map.mapDisplay.getPG();
		float[] xy = map.mapDisplay.getInnerObjectFromLocation(getLocation());
		x = xy[0];
		y = xy[1];

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
	}
	
	/**
	 * Displays this marker's name in a box.
	 */
	public void drawOuter(Map map) {
		if (selected && name != null) {
			PGraphics pg = map.mapDisplay.getOuterPG();
			
			float[] xy = map.mapDisplay.getObjectFromLocation(getLocation());
			float x = xy[0];
			float y = xy[1];
			
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
	public boolean isInside(Map map, float checkX, float checkY) {
		float[] xy = map.mapDisplay.getScreenPositionFromLocation(getLocation());

		selected = PApplet.dist(checkX, checkY, xy[0], xy[1]) < size / 2;
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

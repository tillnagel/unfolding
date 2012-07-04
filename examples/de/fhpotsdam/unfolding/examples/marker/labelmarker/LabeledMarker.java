package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

public class LabeledMarker extends AbstractMarker {

	public String name;
	protected float size;

	public int space=10;
	//public int highlightColor = -256;

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

	public void draw(PGraphics pg, float x, float y) {
	}

	/**
	 * Displays this marker's name in a box.
	 */
	public void drawOuter(PGraphics pg, float x, float y) {
		if (!isVisible()) {
			return;
		}

		pg.pushStyle();
		pg.pushMatrix();
		if(selected){
			pg.translate(0, 0,1);
		}
		pg.strokeWeight(strokeWeight);
		if (selected) {
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
		} else {
			pg.fill(color);
			pg.stroke(strokeColor);
		}
		pg.ellipse(x, y, size, size);// TODO use radius in km and convert to px

		// label
		if (selected && name != null) {
			pg.textFont(font);
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
			pg.rect(x  +strokeWeight/2, y - pg.textSize +strokeWeight/2-space, pg.textWidth(name) + space*1.5f, pg.textSize+space);
			pg.fill(255,255,255);
			pg.text(name, x  +space*0.75f+strokeWeight/2, y +strokeWeight/2-space*0.75f);
		}
		pg.popMatrix();
		pg.popStyle();
		
	}

	/**
	 * Checks whether the given position is in close proximity to this Marker. Used e.g. for indicating whether this
	 * Marker is selected.
	 */
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return PApplet.dist(checkX, checkY, x, y) < size /2;
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

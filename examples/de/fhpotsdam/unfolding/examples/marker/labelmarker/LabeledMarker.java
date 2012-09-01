package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import processing.core.PFont;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

/**
 * Extends point marker to additionally display label. 
 */
public class LabeledMarker extends SimplePointMarker {

	protected String name;
	protected float size;
	protected int space = 10;

	private PFont font;

	public LabeledMarker(Location location, String name, PFont font, float size) {
		this.location = location;
		this.name = name;
		this.font = font;
		this.size = size;
	}

	/**
	 * Displays this marker's name in a box.
	 */
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.pushMatrix();
		if (selected) {
			pg.translate(0, 0, 1);
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
			pg.rect(x + strokeWeight / 2, y - font.getSize() + strokeWeight / 2 - space,
					pg.textWidth(name) + space * 1.5f, font.getSize() + space);
			pg.fill(255, 255, 255);
			pg.text(name, Math.round(x + space * 0.75f + strokeWeight / 2),
					Math.round(y + strokeWeight / 2 - space * 0.75f));
		}
		pg.popMatrix();
		pg.popStyle();
	}

	public String getName() {
		return name;
	}

}

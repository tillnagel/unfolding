package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Marker representing a single location. Use directly to display as simple circle, or extend it for custom styles.
 */
public class SimplePointMarker extends AbstractMarker {

	protected float diameter = 20f;

	/**
	 * Creates an empty point marker. Used internally by the MarkerFactory.
	 */
	public SimplePointMarker() {
		this(null, null);
	}

	/**
	 * Creates a point marker for the given location.
	 * 
	 * @param location
	 *            The location of this Marker.
	 */
	public SimplePointMarker(Location location) {
		this(location, null);
	}

	/**
	 * Creates a point marker for the given location and properties.
	 * 
	 * @param location
	 *            The location of this Marker.
	 * @param properties
	 *            Some data properties for this marker.
	 */
	public SimplePointMarker(Location location, HashMap<String, Object> properties) {
		super(location, properties);
	}

	/**
	 * Draws this point marker as circle in the defined style. If no style has been set, Unfolding's default one is
	 * used.
	 */
	@Override
	public void draw(PGraphics pg, float x, float y) {
		if (isHidden())
			return;

		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		if (isSelected()) {
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
		} else {
			pg.fill(color);
			pg.stroke(strokeColor);
		}
		pg.ellipse((int) x, (int) y, diameter, diameter);
		pg.popStyle();
	}

	@Override
	public boolean isInside(float checkX, float checkY, float x, float y) {
		PVector pos = new PVector(x, y);
		return pos.dist(new PVector(checkX, checkY)) < diameter / 2;
	}

	/**
	 * Sets the radius of this marker. Used for the displayed ellipse and hit test.
	 * 
	 * @param radius The radius of the circle in pixel.
	 * @deprecated Fixed behavior! (value was wrongly used as diameter). Use {@link #setDiameter(float)} instead. 
	 */
	public void setRadius(float radius) {
		this.diameter = radius * 2;
	}
	
	/**
	 * Sets the diameter of this marker. Used for the displayed ellipse and hit test.
	 * 
	 * @param diameter The diameter of the circle in pixel.
	 */
	public void setDiameter(float diameter) {
		this.diameter = diameter;
	}

}

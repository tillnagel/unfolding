package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/**
 * Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public abstract class EarthquakeMarker extends SimplePointMarker {

	// Did the earthquake occur on land? This will be set by the subclasses.
	protected boolean isOnLand;

	// SimplePointMarker has a field "radius" which is inherited
	// by Earthquake marker:
	// protected float radius;
	//
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude.

	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors

	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);

	// constructor
	public EarthquakeMarker(final PointFeature feature) {
		super(feature.getLocation());
		// Add a radius property and then set the properties
		final java.util.HashMap<String, Object> properties = feature.getProperties();
		final float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2 * magnitude);
		setProperties(properties);
		this.radius = 1.75f * getMagnitude();
	}

	// calls abstract method drawEarthquake and then checks age and draws X if
	// needed
	public void draw(final PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();

		// determine color of marker from depth
		colorDetermine(pg);

		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);

		// OPTIONAL TODO: draw X over marker if within past day
		if (getStringProperty("age").equals("Past Day") || getStringProperty("age").equals("Past Hour")) {
			float mag = getMagnitude();

			if (this instanceof OceanQuakeMarker) {
				x = x + mag;
				y = y + mag;

				pg.strokeWeight(2);
				mag = mag * 1.4f;

				pg.line(x - mag, y - mag, x + mag, y + mag);
				pg.line(x + mag, y - mag, x - mag, y + mag);
			} else {
				pg.line(x - mag, y - mag, x + mag, y + mag);
				pg.line(x + mag, y - mag, x - mag, y + mag);
			}
		}

		// reset to previous styling
		pg.popStyle();

	}

	// determine color of marker from depth, and set pg's fill color
	// using the pg.fill method.
	// We suggest: Deep = red, intermediate = blue, shallow = yellow
	// But this is up to you, of course.
	// You might find the getters below helpful.
	private void colorDetermine(final PGraphics pg) {
		// TODO: Implement this method
		final double depth = getDepth();
		final EarthquakeCityMap ecm = new EarthquakeCityMap();
		if (depth > 0 && depth < 70) {
			pg.fill(ecm.yellow);
		} else if (depth > 70 && depth < 300) {
			pg.fill(ecm.blue);
		} else if (depth > 300 && depth < 700) {
			pg.fill(ecm.red);
		}
	}

	/*
	 * getters for earthquake properties
	 */

	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}

	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());
	}

	public String getTitle() {
		return (String) getProperty("title");

	}

	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}

	public boolean isOnLand() {
		return isOnLand;
	}

}

package de.fhpotsdam.unfolding.ui;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class BarScaleUI {
	private static final List<Float> DISPLAY_DISTANCES = Arrays.asList(
			0.01f, 0.02f, 0.05f, 0.1f, 0.2f, 0.5f, 1f, 2f,
			5f, 10f, 20f, 50f, 100f, 200f, 500f, 1000f, 2000f, 5000f);

	private static final float MAX_DISPLAY_DISTANCE = 5000;

	private boolean autoAlignment = true;

	// If false it uses the screen center, resulting in a bar scale depending on
	// the north/south position of the map.
	boolean showDistanceAtEquator = false;

	private PApplet p;
	private UnfoldingMap map;
	public float x;
	public float y;

	// style:
	private int color;
	private int barWeight;
	private int barCapOffset;

	public BarScaleUI(PApplet p, UnfoldingMap map, float x, float y) {
		this.p = p;
		this.map = map;
		this.x = x;
		this.y = y;

		barWeight = 1;
		barCapOffset = 0;
		p.color(255);
		p.textFont(p.createFont("Sans-Serif", 10));
	}

	public BarScaleUI(PApplet p, UnfoldingMap map) {
		this(p, map, 20, map.mapDisplay.getHeight() - 20);
	}

	/**
	 * Draws a bar scale at given position according to current zoom level.
	 *
	 * Calculates distance at equator (scale is dependent on Latitude). Uses a
	 * distance to display from fixed set of distance numbers, so length of bar
	 * may vary.
	 */
	public void draw() {
		// Distance in km, appropriate to current zoom
		float distance = MAX_DISPLAY_DISTANCE / map.getZoom();
		distance = getClosestDistance(distance);

		Location startLocation = null;
		Location destLocation = null;
		if (showDistanceAtEquator) {
			// Get destLocation (world center, on equator, with calculated distance)
			startLocation = new Location(0, 0);
			destLocation = GeoUtils.getDestinationLocation(startLocation, 90f, distance);
		} else {
			startLocation = map.getLocation(p.width / 2, p.height / 2);
			destLocation = GeoUtils.getDestinationLocation(startLocation, 90f, distance);
		}
		// Calculate distance between both locations in screen coordinates
		ScreenPosition destPos = map.getScreenPosition(destLocation);
		ScreenPosition startPos = map.getScreenPosition(startLocation);
		float dx = destPos.x - startPos.x;

		// Display
		p.fill(color);
		if (autoAlignment && x > map.mapDisplay.getWidth() / 2) {
			dx *= -1;
			p.text(PApplet.nfs(distance, 0, 0) + " km", x + 3, y + 4);
		} else {
			p.text(PApplet.nfs(distance, 0, 0) + " km", x + dx + 3, y + 4);
		}
		p.stroke(color);
		p.strokeCap(PApplet.SQUARE);
		p.strokeWeight(barWeight);
		p.line(x - .5f, y, x + .5f + dx, y);
		p.strokeWeight(1);

		int capHeightUp = (barCapOffset == 0) ? 3 : (barCapOffset > 0) ? 0 : -barCapOffset;
		int capHeightDown = (barCapOffset == 0) ? 3 : (barCapOffset < 0) ? 0 : barCapOffset;
		capHeightUp += barWeight / 2;
		capHeightDown += barWeight / 2;

		p.line(x, y - capHeightUp, x, y + capHeightDown);
		p.line(x + dx, y - capHeightUp, x + dx, y + capHeightDown);

	}

	/**
	 * Set the style of the bar scale.
	 *
	 * @param color
	 *            the color of the scale.
	 * @param barWeight
	 *            the weight of the scale.
	 * @param barCapOffset
	 *            the offset of the scale.
	 * @param font
	 *            the font to use for the bar scale.
	 */
	public void setStyle(int color, int barWeight, int barCapOffset, PFont font) {
		this.color = color;
		this.barWeight = barWeight;
		this.barCapOffset = barCapOffset;
		p.textFont(font);
	}

	/**
	 * Returns the nearest distance to display as well as to use for
	 * calculation.
	 * 
	 * @param distance
	 *            The original distance
	 * @return A distance from the set of {@link #DISPLAY_DISTANCES}
	 */
	protected float getClosestDistance(float distance) {
		return closest(distance, DISPLAY_DISTANCES);
	}

	protected float closest(float of, List<Float> in) {
		float min = Float.MAX_VALUE;
		float closest = of;

		for (float v : in) {
			final float diff = Math.abs(v - of);

			if (diff < min) {
				min = diff;
				closest = v;
			}
		}
		return closest;
	}

	public boolean isAutoAlignment() {
		return autoAlignment;
	}

	public void setAutoAlignment(boolean autoAlignment) {
		this.autoAlignment = autoAlignment;
	}

	
}

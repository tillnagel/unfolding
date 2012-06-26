package de.fhpotsdam.unfolding.examples;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Draws a scale bar at he bottom left.
 */
public class MapWithBarScaleApp extends PApplet {

	private static final List<Float> DISPLAY_DISTANCES = Arrays.asList(0.01f, 0.02f, 0.05f, 0.1f, 0.2f, 0.5f, 1f, 2f,
			5f, 10f, 20f, 50f, 100f, 200f, 500f, 1000f, 2000f, 5000f);
	private static final float MAX_DISPLAY_DISTANCE = 5000;

	UnfoldingMap map;

	// If false it uses the screen center, resulting in a bar scale depending on the north/south position of the map.
	boolean showDistanceAtEquator = true;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, "map");
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(createFont("Sans-Serif", 10));
	}

	public void draw() {
		background(0);

		map.draw();

		// Show bar scale at left bottom
		drawBarScale(20, map.mapDisplay.getHeight() - 20);
	}

	/**
	 * Draws a bar scale at given position according to current zoom level.
	 * 
	 * Calculates distance at equator (scale is dependent on Latitude). Uses a distance to display from fixed set of
	 * distance numbers, so length of bar may vary.
	 * 
	 * @param x
	 *            Position to display bar scale
	 * @param y
	 *            Position to display bar scale
	 */
	public void drawBarScale(float x, float y) {

		// Distance in km, appropriate to current zoom
		float distance = MAX_DISPLAY_DISTANCE / map.getZoom();
		distance = getClosestDistance(distance);

		Location startLocation = null;
		Location destLocation = null;
		if (showDistanceAtEquator) {
			// Gets destLocation (world center, on equator, with calculated distance)
			startLocation = new Location(0, 0);
			destLocation = GeoUtils.getDestinationLocation(startLocation, 90f, distance);
		} else {
			startLocation = map.getLocationFromScreenPosition(width / 2, height / 2);
			destLocation = GeoUtils.getDestinationLocation(startLocation, 90f, distance);
		}
		
		// Calculates distance between both locations in screen coordinates
		ScreenPosition dest = map.getScreenPosition(destLocation);
		ScreenPosition start = map.getScreenPosition(startLocation);
		float dx = dest.x - start.x;

		// Display
		stroke(30);
		strokeWeight(1);
		line(x, y - 3, x, y + 3);
		line(x, y, x + dx, y);
		line(x + dx, y - 3, x + dx, y + 3);
		fill(30);
		text(nfs(distance, 0, 0) + " km", x + dx + 3, y + 4);
	}

	/**
	 * Returns the nearest distance to display as well as to use for calculation.
	 * 
	 * @param distance
	 *            The original distance
	 * @return A distance from the set of {@link DISPLAY_DISTANCES}
	 */
	public float getClosestDistance(float distance) {
		return closest(distance, DISPLAY_DISTANCES);
	}

	public float closest(float of, List<Float> in) {
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

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.MapWithBarScaleApp" });
	}
}

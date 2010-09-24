package de.fhpotsdam.unfolding.examples;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MapWithBarScaleApp extends PApplet {

	public static Logger log = Logger.getLogger(MapWithBarScaleApp.class);

	Map map;

	public void setup() {
		size(400, 300, GLConstants.GLGRAPHICS);

		map = new Map(this, "map");
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		textFont(createFont("Sans-Serif", 10));
	}

	public void draw() {
		background(0);

		map.draw();

		// Show 100 km bar
		drawBarScale(20, map.mapDisplay.getHeight() - 20);
	}

	public void drawBarScale(float x, float y) {
		// Scale is dependent on Latitude
		// Calculates distance at equator, and displays it at given position of map

		// Distance in km, appropriate to current zoom
		float distance = 5000 / map.mapDisplay.innerScale;
		distance = getClosestScale(distance);

		Location startLocation = new Location(0, 0);
		Location destLocation = GeoUtils.getDestinationLocation(startLocation, 90f, distance);
		float[] destXY = map.mapDisplay.getScreenPositionFromLocation(destLocation);
		float[] startXY = map.mapDisplay.getScreenPositionFromLocation(startLocation);

		float dx = destXY[0] - startXY[0];

		stroke(30);
		strokeWeight(1);
		line(x, y - 3, x, y + 3);
		line(x, y, x + dx, y);
		line(x + dx, y - 3, x + dx, y + 3);
		fill(30);
		text(nfs(distance, 0, 0) + " km", x + dx + 3, y + 4);
	}

	public float getClosestScale(float scale) {
		List<Float> scales = Arrays.asList(0.01f, 0.02f, 0.05f, 0.1f, 0.2f, 0.5f, 1f, 2f, 5f, 10f,
				20f, 50f, 100f, 200f, 500f, 1000f, 2000f, 5000f);
		return closest(scale, scales);
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

}

package de.fhpotsdam.unfolding.examples.animation;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Animates a map from one position to another.
 * 
 * Uses very simple own animation counter. For smoother animations use some tweening library.
 */
public class SimpleAnimatedMapApp extends PApplet {

	Map map;

	// Berlin
	Location startLocation = new Location(52.5f, 13.4f);

	// London
	Location endLocation = new Location(51.50939f, 0f);

	// Total distance between start and end locations.
	float totalDist = 0;

	// Angle between start and end locations.
	float angle;

	Location tweenLocation;
	float animationCounter = 0;
	boolean animating = false;
	float animationSpeed = 0.05f;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(startLocation, 8);

		// Calculate distance and bearing between the two locations.
		totalDist = (float) GeoUtils.getDistance(startLocation, endLocation);
		angle = degrees((float) GeoUtils.getAngleBetween(startLocation, endLocation));
	}

	public void draw() {
		map.draw();

		if (animating & animationCounter < 1) {
			// Calculate current distance from the normalized animationCounter
			animationCounter += animationSpeed;
			float dist = map(animationCounter, 0, 1, 0, totalDist);
			tweenLocation = GeoUtils.getDestinationLocation(startLocation, angle, dist);
			map.panTo(tweenLocation);
		}
	}

	public void keyPressed() {
		if (key == ' ') {
			// Start/Stop animation
			animating = !animating;
			// And resets to start
			animationCounter = 0;
			map.panTo(startLocation);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.animation.SimpleAnimatedMapApp" });
	}
}
package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * 
 * OUTDATED. Use setTweening(true) now. This is still useful only as example how to do own animation.
 * 
 * Animates a map from one position to another. Uses very simple own animation counter. For smoother animations use some
 * tweening library. Press SPACE to toggle the animation.
 */
public class SimpleAnimatedMapApp extends PApplet {

	UnfoldingMap map;

	// Berlin
	Location startLocation = new Location(52.5f, 13.4f);

	// Hamburg
	Location endLocation = new Location(53.6f, 10);

	// Total distance between start and end locations.
	float totalDist = 0;

	// Angle between start and end locations.
	float angle;

	Location tweenLocation;
	float animationCounter = 0;
	boolean animating = false;
	float animationSpeed = 0.05f;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
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
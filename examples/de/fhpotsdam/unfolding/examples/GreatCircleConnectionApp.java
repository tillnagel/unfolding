package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows a great circle connection between two locations. A great circle
 * connection is an approximation of the shortest route between two places on
 * earth. Displayed as curves in a Mercator projection (as used in Unfolding).
 * 
 * Mouse move and SHIFT or CTRL to move the locations.
 */
public class GreatCircleConnectionApp extends PApplet {

	final Location singaporeLocation = new Location(1, 100);
	final Location berlinLocation = new Location(54, 13.5f);

	Map map;

	Location sourceLocation = berlinLocation;
	Location targetLocation = singaporeLocation;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		fill(0, 0, 255);
		float[] sourcePos = map.getScreenPositionFromLocation(targetLocation);
		ellipse(sourcePos[0], sourcePos[1], 10, 10);

		fill(255, 0, 0);
		float[] targetPos = map.getScreenPositionFromLocation(sourceLocation);
		ellipse(targetPos[0], targetPos[1], 10, 10);

		double bearing = GeoUtils.getAngleBetween(targetLocation,
				sourceLocation);
		double dist = GeoUtils.getDistance(targetLocation, sourceLocation);

		noFill();
		strokeWeight(2);
		stroke(0, 100);
		beginShape();
		for (float d = 0; d < dist; d += 100) {
			Location tweenLocation = GeoUtils.getDestinationLocation(
					targetLocation, degrees((float) bearing), (float) d);
			float[] tweenPos = map.getScreenPositionFromLocation(tweenLocation);
			vertex(tweenPos[0], tweenPos[1]);
		}
		endShape();
	}

	public void mouseMoved() {
		if (keyPressed && key == CODED) {
			if (keyCode == SHIFT) {
				targetLocation = map.getLocationFromScreenPosition(mouseX,
						mouseY);
			}
			if (keyCode == CONTROL) {
				sourceLocation = map.getLocationFromScreenPosition(mouseX,
						mouseY);
			}
		}
	}

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.GreatCircleConnectionApp" });
	}
}
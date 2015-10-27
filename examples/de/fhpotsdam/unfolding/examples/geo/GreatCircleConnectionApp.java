package de.fhpotsdam.unfolding.examples.geo;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a great circle connection between two locations. A great circle connection is an approximation of the shortest
 * route between two places on earth. Displayed as curves in a Mercator projection (as used in Unfolding).
 * 
 * Mouse move and SHIFT or CTRL to move the locations.
 */
public class GreatCircleConnectionApp extends PApplet {

	final Location washingtonLocation = new Location(38.90, -77.04);
	final Location moscowLocation = new Location(55.75, 37.62);

	UnfoldingMap map;

	Location sourceLocation = washingtonLocation;
	Location targetLocation = moscowLocation;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomTo(2);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		noStroke();
		fill(0, 0, 255);
		ScreenPosition sourcePos = map.getScreenPosition(targetLocation);
		ellipse(sourcePos.x, sourcePos.y, 10, 10);

		fill(255, 0, 0);
		ScreenPosition targetPos = map.getScreenPosition(sourceLocation);
		ellipse(targetPos.x, targetPos.y, 10, 10);

		double bearing = GeoUtils.getAngleBetween(targetLocation, sourceLocation);
		double dist = GeoUtils.getDistance(targetLocation, sourceLocation);

		noFill();
		strokeWeight(4);
		stroke(0, 100);
		beginShape();
		for (float d = 0; d < dist; d += 100) {
			Location tweenLocation = GeoUtils.getDestinationLocation(targetLocation, degrees((float) bearing),
					(float) d);
			ScreenPosition tweenPos = map.getScreenPosition(tweenLocation);
			vertex(tweenPos.x, tweenPos.y);
		}
		endShape();
	}

	public void mouseMoved() {
		if (keyPressed && key == CODED) {
			if (keyCode == SHIFT) {
				targetLocation = map.getLocation(mouseX, mouseY);
			}
			if (keyCode == CONTROL) {
				sourceLocation = map.getLocation(mouseX, mouseY);
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { GreatCircleConnectionApp.class.getName() });
	}
}
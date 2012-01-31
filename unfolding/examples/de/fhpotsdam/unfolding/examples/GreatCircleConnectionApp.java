package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class GreatCircleConnectionApp extends PApplet {

	Map map;

	Location singaporeLocation = new Location(1, 100);
	Location berlinLocation = new Location(54, 13.5f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		fill(0, 0, 255);
		float[] sourcePos = map.getScreenPositionFromLocation(singaporeLocation);
		ellipse(sourcePos[0], sourcePos[1], 10, 10);

		fill(255, 0, 0);
		float[] targetPos = map.getScreenPositionFromLocation(berlinLocation);
		ellipse(targetPos[0], targetPos[1], 10, 10);

		double bearing = GeoUtils.getAngleBetween(singaporeLocation, berlinLocation);
		double dist = GeoUtils.getDistance(singaporeLocation, berlinLocation);

		noFill();
		strokeWeight(2);
		stroke(0, 100);
		beginShape();
		for (float d = 0; d < dist; d += 100) {
			Location tweenLocation = GeoUtils.getDestinationLocation(singaporeLocation, degrees((float) bearing),
					(float) d);
			float[] tweenPos = map.getScreenPositionFromLocation(tweenLocation);
			vertex(tweenPos[0], tweenPos[1]);
		}
		endShape();
	}
}

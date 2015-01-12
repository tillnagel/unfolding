package de.fhpotsdam.unfolding.misc;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class FindCentroidApp extends PApplet {

	UnfoldingMap map;
	List<Location> locations = new ArrayList<Location>();

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();

		fill(255, 0, 0, 100);
		beginShape();
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			vertex(pos.x, pos.y);
		}
		endShape();

		Location centroid = GeoUtils.getCentroid(locations);
		ScreenPosition centroidPos = map.getScreenPosition(centroid);
		fill(0, 0, 255);
		ellipse(centroidPos.x, centroidPos.y, 6, 6);

		Location ecentroid = GeoUtils.getEuclideanCentroid(locations);
		ScreenPosition ecentroidPos = map.getScreenPosition(ecentroid);
		fill(0, 255, 0);
		ellipse(ecentroidPos.x, ecentroidPos.y, 6, 6);

		Location mouseLocation = map.getLocation(mouseX, mouseY);
		Location nearestLocation = getNearestLocationTo(centroid);
		if (nearestLocation != null) {
			ScreenPosition nearestPos = map.getScreenPosition(nearestLocation);
			fill(255, 0, 0);
			ellipse(nearestPos.x, nearestPos.y, 10, 10);
		}
	}

	public Location getNearestLocationTo(Location location) {
		double minDistance = Double.MAX_VALUE;
		Location nearestLocation = null;
		for (Location loc : locations) {
			double dist = GeoUtils.getDistance(location, loc);
			if (dist < minDistance) {
				minDistance = dist;
				nearestLocation = loc;
			}
		}
		return nearestLocation;
	}

	public void mouseClicked() {
		Location location = map.getLocation(mouseX, mouseY);
		locations.add(location);
	}

	public void keyPressed() {
		if (key == 'c') {
			locations.clear();
		}
	}

}

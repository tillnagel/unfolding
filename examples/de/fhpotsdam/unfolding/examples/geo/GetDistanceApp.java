package de.fhpotsdam.unfolding.examples.geo;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a circle with a 5km radius.
 */
public class GetDistanceApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(600, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(255);

		map.draw();

		Location mainLocation = new Location(52.52f, 13.42f);
		ScreenPosition pos = map.getScreenPosition(mainLocation);
		float distanceInKm = getDistance(mainLocation, 5);
		fill(255, 255, 0, 127);
		ellipse(pos.x, pos.y, distanceInKm, distanceInKm);
	}

	public float getDistance(Location mainLocation, float size) {
		Location tempLocation = GeoUtils.getDestinationLocation(mainLocation, 90, size);
		ScreenPosition pos1 = map.getScreenPosition(mainLocation);
		ScreenPosition pos2 = map.getScreenPosition(tempLocation);
		return dist(pos1.x, pos1.y, pos2.x, pos2.y);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.geo.GetDistanceApp" });
	}

}

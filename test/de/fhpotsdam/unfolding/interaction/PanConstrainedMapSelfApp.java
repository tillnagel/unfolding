package de.fhpotsdam.unfolding.interaction;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class PanConstrainedMapSelfApp extends PApplet {

	UnfoldingMap map;

	Location centerLocation = new Location(1.359f, 103.816f);

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(centerLocation), 12);
		map.setZoomRange(12, 15);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		
		restrictMap(new Location(1.359f, 103.816f), 30);
		map.draw();
	}

	private void restrictMap(Location restrictedPanLocation, float maxPanningDistance) {
		Location mapCenter = map.getCenter();
		double dist = GeoUtils.getDistance(restrictedPanLocation, mapCenter);
		if (dist > maxPanningDistance) {
			float angle = PApplet.degrees((float) getAngleBetween(restrictedPanLocation, mapCenter));
			float backDist = maxPanningDistance - (float) dist;
			// Pan back, with same angle but negative distance
			Location newLocation = GeoUtils.getDestinationLocation(mapCenter, angle, backDist);
			map.panTo(newLocation);
		}
	}
	
	public static double getAngleBetween(Location location1, Location location2) {
		double rlat1 = Math.toRadians(location1.getLat());
		double rlat2 = Math.toRadians(location2.getLat());
		double rlon1 = Math.toRadians(location1.getLon());
		double rlon2 = Math.toRadians(location2.getLon());

		double angle = (Math.atan2(Math.sin(rlon2 - rlon1) * Math.cos(rlat2),
				Math.cos(rlat1) * Math.sin(rlat2) - Math.sin(rlat1) * Math.cos(rlat2) * Math.cos(rlon2 - rlon1)) % (2 * Math.PI));

		return angle;
	}
	
}

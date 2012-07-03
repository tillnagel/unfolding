package de.fhpotsdam.unfolding.examples.marker.multimarker;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;

public class DistancePerLocationPolygonMarker extends SimplePolygonMarker{

	@Override
	public double getDistanceTo(Location location) {
		double minDistance = Double.MAX_VALUE;
		for(Location loc : locations){
			double dist = GeoUtils.getDistance(location, loc);
			minDistance = dist < minDistance ? dist : minDistance;
		}
		return minDistance;
	}
}

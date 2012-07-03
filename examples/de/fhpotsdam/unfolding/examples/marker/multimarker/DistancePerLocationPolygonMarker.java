package de.fhpotsdam.unfolding.examples.marker.multimarker;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;

public class DistancePerLocationPolygonMarker extends SimplePolygonMarker{

	public DistancePerLocationPolygonMarker() {
		super();
	}

	public DistancePerLocationPolygonMarker(List<Location> locations) {
		super(locations);
	}

	public DistancePerLocationPolygonMarker(List<Location> locations, HashMap<String, Object> properties) {
		super(locations, properties);
	}
	
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

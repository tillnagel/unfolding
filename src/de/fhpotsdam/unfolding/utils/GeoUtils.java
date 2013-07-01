package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import processing.core.PVector;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MultiFeature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;

/**
 * Basic geo-spatial utility methods.
 */
public class GeoUtils {

	private static final double EARTH_RADIUS_KM = 6371.01;

	/**
	 * Get distance in kilometers between two points on the earth. Using the great-circle distance formula with the
	 * approximated radius of a spherical earth.
	 * 
	 * @param lat1
	 *            Latitude of first point, in decimal degrees.
	 * @param lon1
	 *            Longitude of first point, in decimal degrees.
	 * @param lat2
	 *            Latitude of second point, in decimal degrees.
	 * @param lon2
	 *            Longitude of second point, in decimal degrees.
	 * @return Distance in kilometers.
	 */
	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
		double lat1Rad = Math.toRadians(lat1);
		double lon1Rad = Math.toRadians(lon1);
		double lat2Rad = Math.toRadians(lat2);
		double lon2Rad = Math.toRadians(lon2);

		double r = EARTH_RADIUS_KM;
		return r
				* Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad)
						* Math.cos(lon2Rad - lon1Rad));
	}

	/**
	 * Get distance in kilometers between two points on the earth. Using the great-circle distance formula with the
	 * approximated radius of a spherical earth.
	 * 
	 * @param location1
	 *            Location of first point
	 * @param location2
	 *            Location of second point
	 * @return Distance in kilometers.
	 */
	public static double getDistance(Location location1, Location location2) {
		return getDistance(location1.getLat(), location1.getLon(), location2.getLat(), location2.getLon());
	}

	/**
	 * Gets the location specified by a start location, a bearing, and a distance.
	 * 
	 * @param location
	 *            The start location.
	 * @param bearing
	 *            The bearing in degrees.
	 * @param distance
	 *            The distance in kilometers.
	 * @return The destination location.
	 */
	public static Location getDestinationLocation(Location location, float bearing, float distance) {
		double lat1 = Math.toRadians(location.getLat());
		double lon1 = Math.toRadians(location.getLon());
		double r = EARTH_RADIUS_KM;
		double b = Math.toRadians(bearing);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance / r) + Math.cos(lat1) * Math.sin(distance / r)
				* Math.cos(b));
		double lon2 = lon1
				+ Math.atan2(Math.sin(b) * Math.sin(distance / r) * Math.cos(lat1),
						Math.cos(distance / r) - Math.sin(lat1) * Math.sin(lat2));
		lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

		float lat2d = (float) Math.toDegrees(lat2);
		float lon2d = (float) Math.toDegrees(lon2);

		return new Location(lat2d, lon2d);
	}

	/**
	 * Gets the angle between two locations.
	 * 
	 * @param location1
	 *            First location.
	 * @param location2
	 *            Second location.
	 * @return The angle in radians.
	 */
	public static double getAngleBetween(Location location1, Location location2) {
		double rlat1 = Math.toRadians(location1.getLat());
		double rlat2 = Math.toRadians(location2.getLat());
		double rlon1 = Math.toRadians(location1.getLon());
		double rlon2 = Math.toRadians(location2.getLon());

		double angle = (Math.atan2(Math.sin(rlon2 - rlon1) * Math.cos(rlat2),
				Math.cos(rlat1) * Math.sin(rlat2) - Math.sin(rlat1) * Math.cos(rlat2) * Math.cos(rlon2 - rlon1)) % (2 * Math.PI));

		return angle;
	}

	/**
	 * Super simplistic method to convert a geo-position as a Location.
	 */
	public static Location getDecimal(Integer latDegrees, Integer latMinutes, Integer latSeconds, String latDirection,
			Integer lonDegrees, Integer lonMinutes, Integer lonSeconds, String lonDirection) {
		float lat = latDegrees + (latMinutes * 60 + latSeconds) / 3600;
		if (latDirection.equals("S")) {
			lat = -lat;
		}
		float lon = lonDegrees + (lonMinutes * 60 + lonSeconds) / 3600;
		if (lonDirection.equals("W")) {
			lon = -lon;
		}
		return new Location(lat, lon);
	}

	/**
	 * Returns the center of the locations.
	 * 
	 * The returned location minimizes the sum of squared Euclidean distances between itself and each location in the
	 * list.
	 * 
	 * @return The centroid location.
	 */
	public static Location getEuclideanCentroid(List<Location> locations) {
		Location center = new Location(0, 0);
		for (Location loc : locations) {
			center.add(loc);
		}
		center.div((float) locations.size());
		return center;
	}

	/**
	 * Returns the geometric center of the locations of a polygon.
	 * 
	 * The returned location is the center of the polygon, unfazed by unbalanced vertices. (You probably want to use
	 * this algorithm, but check out {@link #getEuclideanCentroid(List)} for an alternative.)
	 * 
	 * @return The centroid location.
	 */
	public static Location getCentroid(List<Location> originalVertices) {
		List<Location> vertices = getClosedPolygon(originalVertices);
		float cx = 0f, cy = 0f;
		for (int i = 0; i < vertices.size() - 1; i++) {
			PVector vi0 = vertices.get(i);
			PVector vi1 = vertices.get(i + 1);
			cx = cx + (vi0.x + vi1.x) * (vi0.x * vi1.y - vi0.y * vi1.x);
			cy = cy + (vi0.y + vi1.y) * (vi0.x * vi1.y - vi0.y * vi1.x);
		}
		float area = getArea(vertices);
		cx /= (6f * area);
		cy /= (6f * area);
		return new Location(cx, cy);
	}

	/**
	 * Returns a closed polygon with the last vertex the same as the first.
	 * 
	 * @param originalVertices
	 *            List of locations of original polygon.
	 * @return List of location of closed polygon.
	 */
	protected static List<Location> getClosedPolygon(List<Location> originalVertices) {
		if (originalVertices.size() < 1
				|| (originalVertices.get(0).equals(originalVertices.get(originalVertices.size() - 1)))) {
			// Return unchanged, if only one point, or already closed
			return originalVertices;
		}

		List<Location> vertices = new ArrayList<Location>(originalVertices.size() + 1);
		for (int i = 0; i < originalVertices.size(); i++) {
			vertices.add(new Location(0f, 0f));
		}
		Collections.copy(vertices, originalVertices);
		if (vertices.size() > 1) {
			if (!vertices.get(0).equals(vertices.get(vertices.size() - 1))) {
				// Add first vertex on last position to close polygon
				vertices.add(vertices.get(0));
			}
		}
		return vertices;
	}

	/**
	 * Calculates the area of a polygon.
	 * 
	 * @param vertices
	 *            The vertices of the polygon.
	 * @return The area.
	 */
	protected static float getArea(List<Location> vertices) {
		float sum = 0;
		for (int i = 0; i < vertices.size() - 1; i++) {
			PVector vi0 = vertices.get(i);
			PVector vi1 = vertices.get(i + 1);
			sum += (vi0.x * vi1.y - vi1.x * vi0.y);
		}
		return sum * 0.5f;
	}

	/**
	 * Calculates the area of a polygon feature.
	 * 
	 * @param feature
	 *            The feature containing vertices.
	 * @return The area.
	 */
	protected static float getArea(Feature feature) {
		return getArea(GeoUtils.getLocations(feature));
	}

	/**
	 * Gets the overall geometric center of all features.
	 * 
	 * @param features
	 *            A list of features.
	 * @return The centroid location.
	 */
	public static Location getCentroidFromFeatures(List<Feature> features) {
		return GeoUtils.getCentroid(GeoUtils.getLocationsFromFeatures(features));
	}

	/**
	 * Returns the centroid of a feature. Returns the single location for a PointFeature, the geometric center for
	 * LineFeature and PolygonFeature, and &mdash; depending on the second parameter &mdash; either the overall
	 * geometric center of all features, or the geometric center of the largest feature in a MultiFeature.
	 * 
	 * @param feature
	 *            The feature to get the centroid for.
	 * @param useLargestForMulti
	 *            Set to true if you want to use only the largest feature for {@link MultiFeature}s.
	 * @return The location of the geometric center.
	 */
	public static Location getCentroid(Feature feature, boolean useLargestForMulti) {
		Location location = null;

		switch (feature.getType()) {
		case POINT:
			location = ((PointFeature) feature).getLocation();
			break;
		case LINES:
		case POLYGON:
			location = GeoUtils.getCentroid(((ShapeFeature) feature).getLocations());
			break;
		case MULTI:
			MultiFeature multiFeature = ((MultiFeature) feature);
			if (useLargestForMulti) {

				// Return centroid of largest feature
				float largestArea = 0;
				Feature largestFeature = null;
				for (Feature f : multiFeature.getFeatures()) {
					if (largestArea < getArea(f)) {
						largestFeature = f;
						largestArea = getArea(f);
					}
				}
				location = getCentroid(largestFeature);

			} else {

				// Return centroid of all features
				List<Location> locations = new ArrayList<Location>();
				for (Feature f : multiFeature.getFeatures()) {
					Location l = getCentroid(f);
					locations.add(l);
				}
				location = GeoUtils.getCentroid(locations);
			}
			break;
		}

		return location;
	}

	/**
	 * Convenience method for {@link #getCentroid(Feature, boolean)}.
	 */
	public static Location getCentroid(Feature feature) {
		return getCentroid(feature, false);
	}

	/**
	 * Returns all locations of all features.
	 * 
	 * @param features
	 *            A list of features.
	 * @return A list of locations.
	 */
	public static List<Location> getLocationsFromFeatures(List<Feature> features) {
		List<Location> locations = new ArrayList<Location>();
		for (Feature feature : features) {
			locations.addAll(getLocations(feature));
		}
		return locations;
	}

	/**
	 * Returns all locations of a feature. That is a single location for a point, all locations for lines or polygons,
	 * and all locations of all features of a MultiFeature.
	 * 
	 * @param feature
	 *            The feature to get locations from.
	 * @return A list of locations.
	 */
	public static List<Location> getLocations(Feature feature) {
		List<Location> locations = new ArrayList<Location>();
		if (feature.getType() == Feature.FeatureType.POINT) {
			PointFeature pf = (PointFeature) feature;
			locations.add(pf.getLocation());
		}
		if (feature.getType() == Feature.FeatureType.LINES || feature.getType() == Feature.FeatureType.POLYGON) {
			ShapeFeature sf = (ShapeFeature) feature;
			locations.addAll(sf.getLocations());
		}
		if (feature.getType() == Feature.FeatureType.MULTI) {
			MultiFeature multiFeature = (MultiFeature) feature;
			for (Feature f : multiFeature.getFeatures()) {
				locations.addAll(getLocations(f));
			}
		}
		return locations;
	}

	/**
	 * Returns all locations of all markers.
	 * 
	 * @param markers
	 *            A list of markers.
	 * @return A list of locations.
	 */
	public static List<Location> getLocationsFromMarkers(List<Marker> markers) {
		List<Location> locations = new ArrayList<Location>();
		for (Marker marker : markers) {
			locations.addAll(getLocations(marker));
		}
		return locations;
	}

	/**
	 * Returns all locations of a marker. That is a single location for a point, all locations for lines or polygons,
	 * and all locations of all markers of a MultiMarker.
	 * 
	 * @param marker
	 *            The marker to get locations from.
	 * @return A list of locations.
	 */
	public static List<Location> getLocations(Marker marker) {
		List<Location> locations = new ArrayList<Location>();
		if (marker instanceof MultiMarker) {
			// recursive for multi
			MultiMarker mm = (MultiMarker) marker;
			for (Marker m : mm.getMarkers()) {
				locations.addAll(getLocations(m));
			}
		} else if (marker instanceof AbstractShapeMarker) {
			// line or polygon
			AbstractShapeMarker sm = (AbstractShapeMarker) marker;
			locations.addAll(sm.getLocations());
		} else {
			// default: point
			locations.add(marker.getLocation());
		}
		return locations;
	}

}

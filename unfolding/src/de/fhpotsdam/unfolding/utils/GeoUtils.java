package de.fhpotsdam.unfolding.utils;

import de.fhpotsdam.unfolding.geo.Location;

/**
 * Basic geo-spatial utility methods.
 */
public class GeoUtils {

	private static final double EARTH_RADIUS_KM = 6371.01;
	private static final double RAD_CONV = 180 / Math.PI;

	/**
	 * Get distance in kilometers between two points on the earth. Using the great-circle distance
	 * formula with the approximated radius of a spherical earth.
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
		double lat1Rad = getRadians(lat1);
		double lon1Rad = getRadians(lon1);
		double lat2Rad = getRadians(lat2);
		double lon2Rad = getRadians(lon2);

		double r = EARTH_RADIUS_KM;
		return r
				* Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad)
						* Math.cos(lat2Rad) * Math.cos(lon2Rad - lon1Rad));
	}

	public static double getDistance(Location location1, Location location2) {
		return getDistance(location1.getLat(), location1.getLon(), location2.getLat(), location2
				.getLon());
	}

	public static Location getDestinationLocation(Location location, float bearing, float distance) {
		double lat1 = getRadians(location.getLat());
		double lon1 = getRadians(location.getLon());
		double r = EARTH_RADIUS_KM;
		double b = getRadians(bearing);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance / r) + Math.cos(lat1)
				* Math.sin(distance / r) * Math.cos(b));
		double lon2 = lon1
				+ Math.atan2(Math.sin(b) * Math.sin(distance / r) * Math.cos(lat1), Math
						.cos(distance / r)
						- Math.sin(lat1) * Math.sin(lat2));
		lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

		float lat2d = (float) getDegree(lat2);
		float lon2d = (float) getDegree(lon2);

		return new Location(lat2d, lon2d);
	}

	public static double getRadians(double angle) {
		return angle / RAD_CONV;
	}

	public static double getDegree(double rad) {
		return rad * RAD_CONV;
	}

}

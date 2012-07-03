package de.fhpotsdam.unfolding.utils;

import de.fhpotsdam.unfolding.geo.Location;

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
}

package de.fhpotsdam.unfolding.utils;

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

	public static double getRadians(double angle) {
		return angle / RAD_CONV;
	}

}

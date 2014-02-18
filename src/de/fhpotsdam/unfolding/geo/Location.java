package de.fhpotsdam.unfolding.geo;

import de.fhpotsdam.unfolding.utils.GeoUtils;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * The Location represents a geo location defined by latitude and longitude.
 */
public class Location extends PVector {

	/**
	 * Create a Location.
	 * @param lat latitude of the location.
	 * @param lon longitude of the location
	 */
	public Location(float lat, float lon) {
		this.x = lat;
		this.y = lon;
	}
	
	public Location(double lat, double lon) {
		this((float)lat,(float)lon);
	}
	
	/**
	 * Create a Location
	 * @param location location to use.
	 */
	public Location(Location  location){
		this.x = location.x;
		this.y = location.y;
	}

	public float getLat() {
		return x;
	}

	public void setLat(float lat) {
		this.x = lat;
	}

	public float getLon() {
		return y;
	}

	public void setLon(float lon) {
		this.y = lon;
	}

	public String toString() {
		return "(" + PApplet.nf(x, 1, 3) + ", " + PApplet.nf(y, 1, 3) + ")";
	}
	
	public double getDistance(Location otherLocation) {
		return GeoUtils.getDistance(this, otherLocation);
	}

}

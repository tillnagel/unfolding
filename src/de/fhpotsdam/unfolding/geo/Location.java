package de.fhpotsdam.unfolding.geo;

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
	
	public Location getConstrained() {
		Location l = new Location(this);
		float lat = l.getLat() % 360.0f;
		float lon = l.getLon() % 360.0f;
		if(lat!=0)lat=(PApplet.abs(lat)>180.0f) ?  lat % 180.0f+(180.0f*-lat/PApplet.abs(lat)) : lat;
		if(lon!=0)lon=(PApplet.abs(lon)>180.0f) ?  lon % 180.0f+(180.0f*-lon/PApplet.abs(lon)) : lon;
		l.setLat(lat);
		l.setLon(lon);
		return l;
	}
	
	public void constrain() {
		this.x=this.getConstrained().x;
		this.y=this.getConstrained().y;
	}

}

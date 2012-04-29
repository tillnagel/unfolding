package de.fhpotsdam.unfolding.geo;

import processing.core.*;

public class Location extends PVector {

	public Location(float lat, float lon) {
		this.x = lat;
		this.y = lon;
	}
	
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

}

package de.fhpotsdam.unfolding.performance;

import java.util.Date;

import de.fhpotsdam.unfolding.geo.Location;

public class Dot {

	public Location location;
	public Date timestamp;

	public Dot(Location location, Date timestamp) {
		this.location = location;
		this.timestamp = timestamp;
	}

}

package de.fhpotsdam.unfolding.data;

import de.fhpotsdam.unfolding.geo.Location;

public class PointFeature extends Feature {

	public Location location;

	public PointFeature(Location location) {
		super(FeatureType.POINT);
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}

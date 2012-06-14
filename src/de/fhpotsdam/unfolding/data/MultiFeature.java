package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;

public class MultiFeature extends Feature {

	public List<Location> locations = new ArrayList<Location>();

	public MultiFeature(FeatureType type) {
		super(type);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void addLocation(Location location) {
		locations.add(location);
	}

	public FeatureType getType() {
		return FeatureType.LINES;
	}

}

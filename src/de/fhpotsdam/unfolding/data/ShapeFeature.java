package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;

/**
 * Stores a list of locations. Is used for lines and polygons.
 */
public class ShapeFeature extends Feature {

	public List<Location> locations = new ArrayList<Location>();

	public ShapeFeature(FeatureType type) {
		super(type);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void addLocation(Location location) {
		locations.add(location);
	}

}

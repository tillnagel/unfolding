package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;

/**
 * Stores a list of locations. Is used for lines and polygons.
 */
public class ShapeFeature extends Feature {

	/** The list of locations. */
	public List<Location> locations = new ArrayList<Location>();

	/**
	 * Creates a shape feature with the type.
	 * 
	 * @param type
	 *            The type. Should be either {@link Feature.FeatureType#LINES} or {@link Feature.FeatureType#POLYGON}.
	 */
	public ShapeFeature(FeatureType type) {
		super(type);
	}

	/**
	 * Returns all locations of this shape feature.
	 * 
	 * @return The locations.
	 */
	public List<Location> getLocations() {
		return locations;
	}

	/**
	 * Adds a location to this shape feature. Will be added at the end of the list.
	 * 
	 * @param location
	 *            A location.
	 */
	public void addLocation(Location location) {
		locations.add(location);
	}

}

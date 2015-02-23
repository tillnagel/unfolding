package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;

/**
 * Stores a list of locations. Is used for lines and polygons.
 * 
 * Optionally stores interior rings for polygons with holes.
 */
public class ShapeFeature extends Feature {

	/** The list of locations (exterior ring). */
	public List<Location> locations = new ArrayList<Location>();

	/** Optional interior rings (polygon holes). */
	public List<List<Location>> interiorRingLocationArray = null;

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

	/**
	 * Adds a list of locations as interior ring.
	 * 
	 * @param interiorRingLocations
	 *            The locations of the interior ring.
	 */
	public void addInteriorRing(List<Location> interiorRingLocations) {
		if (interiorRingLocationArray == null) {
			interiorRingLocationArray = new ArrayList<List<Location>>();
		}

		interiorRingLocationArray.add(interiorRingLocations);
	}

	/**
	 * Returns all interior rings of the shape.
	 * 
	 * @return A list of rings, i.e. a list of lists of locations, or null if shape has no interior rings.
	 */
	public List<List<Location>> getInteriorRings() {
		return interiorRingLocationArray;
	}

}

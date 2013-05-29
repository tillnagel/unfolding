package de.fhpotsdam.unfolding.data;

import de.fhpotsdam.unfolding.geo.Location;

/**
 * Stores a single location.
 */
public class PointFeature extends Feature {

	/** The location of this point feature. */
	public Location location;

	/**
	 * Creates a PointFeature. Its location needs to be set later on.
	 */
	public PointFeature() {
		super(FeatureType.POINT);
	}

	/**
	 * Creates a PointFeature with a single location.
	 * 
	 * @param location
	 *            The location.
	 */
	public PointFeature(Location location) {
		super(FeatureType.POINT);
		this.location = location;
	}

	/**
	 * Returns the single location of this point feature.
	 * 
	 * @return A location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the single location of this point feature.
	 * 
	 * @param location
	 *            A location.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

}

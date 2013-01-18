package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Abstract marker representing multiple locations. Can be implemented as lines, polygons, or other shapes.
 * 
 * Handles multiple locations, and implements the main marker methods for handling those properly.
 */
public abstract class AbstractShapeMarker extends AbstractMarker {

	/** All locations defining this shape. */
	protected List<Location> locations;

	public AbstractShapeMarker() {
		this(new ArrayList<Location>(), null);
	}

	/**
	 * Creates a shape marker for the given locations.
	 * 
	 * @param location
	 *            The list of locations.
	 */
	public AbstractShapeMarker(List<Location> locations) {
		this(locations, null);
	}

	/**
	 * Creates a shape marker for the given locations.
	 * 
	 * @param location
	 *            The list of locations.
	 * @param properties
	 *            Some data properties for this marker.
	 */
	public AbstractShapeMarker(List<Location> locations, HashMap<String, Object> properties) {
		this.locations = locations;
		setProperties(properties);
	}

	// Methods handling locations -------------------------

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void addLocations(Location... locations) {
		for (Location loc : locations) {
			this.locations.add(loc);
		}
	}

	public void addLocations(List<Location> locations) {
		this.locations.addAll(locations);
	}

	public void addLocation(float x, float y) {
		locations.add(new Location(x, y));
	}

	/**
	 * Returns the geometric center of this shape.
	 * 
	 * The returned location minimizes the sum of squared Euclidean distances between itself and each location in the
	 * list.
	 * 
	 * @return The centroid location.
	 */
	public Location getCentroid() {
		return GeoUtils.getCentroid(locations);
	}

	public Location getLocation(int index) {
		return locations.get(index);
	}

	public void removeLocation(Location location) {
		locations.remove(location);
	}

	public void removeLocation(int index) {
		locations.remove(index);
	}

	/**
	 * Adds the given location to the list of locations.
	 * 
	 * NB: Does not set this as the only location! This implementation is just prevent mistakes by using this method as
	 * more general Marker method.
	 * 
	 * @param location
	 *            The location to add.
	 */
	@Override
	public void setLocation(Location location) {
		addLocations(location);
	}

	@Override
	public Location getLocation() {
		return getCentroid();
	}

	@Override
	public void draw(UnfoldingMap map) {
		PGraphics pg = map.mapDisplay.getOuterPG();

		List<MapPosition> mapPositions = new ArrayList<MapPosition>();

		for (Location loc : getLocations()) {
			float[] xy = map.mapDisplay.getObjectFromLocation(loc);
			mapPositions.add(new MapPosition(xy));
		}

		draw(pg, mapPositions, properties, map);
	}

	protected void draw(PGraphics pg, List<MapPosition> mapPositions, HashMap<String, Object> properties,
			UnfoldingMap map) {
		draw(pg, mapPositions);
	}

	/**
	 * Draws these markers in outer object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param objectPositions
	 *            The positions in outer object coordinates.
	 */
	public abstract void draw(PGraphics pg, List<MapPosition> objectPositions);

	@Override
	public void draw(PGraphics pg, float x, float y) {

	}

	// REVISIT default behavior for getLocation(), draw(location),
	// drawOuter(location)?

	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		List<ScreenPosition> positions = new ArrayList<ScreenPosition>();
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			positions.add(pos);
		}
		return isInside(checkX, checkY, positions);
	}

	protected boolean isInside(float checkX, float checkY,
			List<? extends PVector> vectors) {
		boolean inside = false;
		for (int i = 0, j = vectors.size() - 1; i < vectors.size(); j = i++) {
			PVector pi = vectors.get(i);
			PVector pj = vectors.get(j);
			if ((((pi.y <= checkY) && (checkY < pj.y)) || ((pj.y <= checkY) && (checkY < pi.y)))
					&& (checkX < (pj.x - pi.x) * (checkY - pi.y) / (pj.y - pi.y) + pi.x)) {
				inside = !inside;
			}
		}
		return inside;
	}

	/**
	 * Checks whether given position is inside this marker, according to the
	 * shape defined by the marker's locations
	 * 
	 * @param longitude
	 *            The longitude
	 * @param latitude
	 *            The latitude
	 */
	public boolean isInsideByLocation(float latitude, float longitude) {
		return isInside(latitude, longitude, locations);
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		// TODO Simply return false?
		throw new RuntimeException("Check for a single positon is not implemented for polygons.");
	}

}

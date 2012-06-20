package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Marker interface for all markers to be drawn on to maps.
 * 
 * A marker has at least one location, and properties. A marker can be drawn, selected, and tested if hit.
 */
public interface Marker {

	/**
	 * Gets the location of this marker.
	 * 
	 * For point marker this returns the single location, for shape markers it should return a meaningful location, e.g.
	 * the centroid.
	 * 
	 * @return The location with lat, lng.
	 */
	public Location getLocation();

	/**
	 * Set the location for this marker.
	 * 
	 * @param lat
	 *            latitude
	 * @param lng
	 *            longitude
	 */
	public void setLocation(float lat, float lng);

	/**
	 * Set the location for this marker.
	 * 
	 * @param location
	 *            Location with lat, lng
	 */
	public void setLocation(Location location);

	/**
	 * Calculate distance to location
	 * 
	 * @param location
	 * @return distance to location
	 * 
	 */
	public double getDistanceTo(Location location);

	/**
	 * Sets the properties.
	 * 
	 * @param props
	 */
	public void setProperties(HashMap<String, Object> props);

	public HashMap<String, Object> getProperties();

	/**
	 * Checks whether given position is inside this marker, according to the maps coordinate system.
	 * 
	 * @param map
	 *            The map to draw on.
	 * @param checkX
	 *            The x position to check in screen coordinates.
	 * @param checkY
	 *            The y position to check in screen coordinates.
	 * @return true if inside, false otherwise.
	 */
	public boolean isInside(Map map, float checkX, float checkY);

	/**
	 * Draws this marker in inner object coordinate system.
	 * 
	 * e.g. markers oriented to the tiles
	 * 
	 * @param map
	 *            The map to draw on.
	 */
	public void draw(Map map);

	/**
	 * Draws this marker in outer object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param map
	 *            The map to draw on.
	 */
	public void drawOuter(Map map);

	public void setSelected(boolean selected);

	public boolean isSelected();

	// For drawing onto the texture, i.e. after distortion, etc.
	// public void drawTexture(PGraphics pg);

}

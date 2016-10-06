package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Marker interface for all markers to be drawn on to maps.
 * 
 * A marker has at least one location, and properties. A marker can be drawn, selected, and tested if hit.
 */
public interface Marker {

	/**
	 * Gets the marker ID.
	 * 
	 * @return An ID.
	 */
	public String getId();

	/**
	 * Sets the marker ID.
	 * 
	 * @param id
	 *            The ID.
	 */
	public void setId(String id);

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
	 * Calculate distance between this marker and the given location.
	 * 
	 * @param location
	 *            The location to calculate the distance to.
	 * @return Distance to location in kilometers (km).
	 * 
	 */
	public double getDistanceTo(Location location);

	/**
	 * Sets the optional properties of this marker.
	 * 
	 * @param properties
	 *            The properties to set. The map consist of key,value pairs for each property.
	 */
	public void setProperties(HashMap<String, Object> properties);

	/**
	 * Sets the property for given key. If properties previously contained a mapping for the key the value is replaced.
	 * 
	 * @param key
	 *            The key of the property.
	 * @param value
	 *            The value of the property.
	 * @return The previous value, or null.
	 */
	public Object setProperty(String key, Object value);

	/**
	 * Gets the optional properties of this marker. The map consist of key,value pairs for each property.
	 * 
	 * @return A map of properties.
	 */
	public HashMap<String, Object> getProperties();

	/**
	 * Gets the property for given key.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return The property, if found.
	 */
	public Object getProperty(String key);

	/**
	 * Gets the property as String for given key.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return The property, if found.
	 */
	public String getStringProperty(String key);

	/**
	 * Gets the property as Integer for given key.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return The property, if found.
	 */
	public Integer getIntegerProperty(String key);

	/**
	 * Checks whether given position is inside this marker, according to the maps coordinate system. Can be used for
	 * interactive hit tests, e.g. mouse interaction
	 * 
	 * @param map
	 *            The map to draw on.
	 * @param checkX
	 *            The x position to check in screen coordinates.
	 * @param checkY
	 *            The y position to check in screen coordinates.
	 * @return true if inside, false otherwise.
	 */
	public boolean isInside(UnfoldingMap map, float checkX, float checkY);

	/**
	 * Draws this marker.
	 * 
	 * @param map
	 *            The map to draw on.
	 */
	public void draw(UnfoldingMap map);

	/**
	 * Changes the select status of this marker. Sub-classes can use the selection status to highlight this marker.
	 * 
	 * @param selected
	 *            Whether this marker is selected or not.
	 */
	public void setSelected(boolean selected);

	/**
	 * Indicates whether this marker is selected. Can be used for drawing the marker differently, e.g. to highlight it.
	 * 
	 * @return true if it is selected, false otherwise.
	 */
	public boolean isSelected();

	/**
	 * Sets the visibility status of this marker.
	 * 
	 * @param hidden
	 *            The new status
	 */
	public void setHidden(boolean hidden);

	/**
	 * Indicates whether this marker is hidden. <b>Can</b> be used for drawing the marker differently.
	 * 
	 * @return true if it is hidden, false otherwise.
	 */
	public boolean isHidden();

	/**
	 * Sets the main color of this marker.
	 * 
	 * @param color
	 *            The color (in Processing's color type)
	 */
	public void setColor(int color);

	/**
	 * Sets the color of this marker's border.
	 * 
	 * @param color
	 *            The color (in Processing's color type)
	 */
	public void setStrokeColor(int color);

	/**
	 * Sets the thickness of the border of this marker.
	 * 
	 * @param weight
	 *            Thickness in pixel.
	 */
	public void setStrokeWeight(int weight);

	/**
	 * Sets the highlight color of this marker. In the default implementations (SimpleXxMarker), this color is used if
	 * the marker is selected.
	 * 
	 * @param highlightColor
	 *            The color (in Processing's color type)
	 */
	public void setHighlightColor(int highlightColor);

	/**
	 * Sets the highlight color of this marker's border. In the default implementations (SimpleXxMarker), this color is
	 * used if the marker is selected.
	 * 
	 * @param highlightColor
	 *            The color (in Processing's color type)
	 */
	public void setHighlightStrokeColor(int highlightStrokeColor);

}

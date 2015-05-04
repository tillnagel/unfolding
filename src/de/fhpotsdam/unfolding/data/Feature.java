package de.fhpotsdam.unfolding.data;

import java.util.HashMap;

/**
 * A feature stores one or more locations, its type, and additional data properties.
 */
public class Feature {

	public static enum FeatureType {
		POINT, LINES, POLYGON, MULTI
	}

	private FeatureType type;
	private String id;

	/** Stores data properties. A feature does not know about the semantics though, this has to be done in the display. */
	public HashMap<String, Object> properties = new HashMap<String, Object>();

	/**
	 * Creates a feature for a specific type.
	 * 
	 * @param type
	 *            The type of this feature.
	 */
	public Feature(FeatureType type) {
		this.type = type;
	}

	/**
	 * Returns the type of this feature.
	 * 
	 * @return The type.
	 */
	public FeatureType getType() {
		return type;
	}

	/**
	 * Returns all data properties.
	 * 
	 * @return The properties.
	 */
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Returns a property for the key.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return A property or null if not found.
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * Adds a property to this feature.
	 * 
	 * @param key
	 *            The key of this property.
	 * @param value
	 *            The value of this property.
	 */
	public Object addProperty(String key, Object value) {
		return properties.put(key, value);
	}

	/**
	 * Adds a property to this feature.
	 * 
	 * @param key
	 *            The key of this property.
	 * @param value
	 *            The value of this property.
	 */
	public void putProperty(String key, Object value) {
		properties.put(key, value);
	}

	/**
	 * Returns the value of a String property.
	 * 
	 * This only returns a non-null value if the value is a String. For String representations of other objects use
	 * {@link #getProperty(String)} and convert it yourself.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return The value as String, or null if not found or if property is not a String.
	 */
	public String getStringProperty(String key) {
		Object value = properties.get(key);
		if (value != null && value instanceof String) {
			return (String) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the value of a Integer property.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return The value as Integer, or null if not found or if property is not an Integer.
	 */
	public Integer getIntegerProperty(String key) {
		Object value = properties.get(key);
		if (value != null && value instanceof Integer) {
			return (Integer) value;
		} else {
			return null;
		}
	}

	/**
	 * Sets all properties. All previously existing ones will be removed.
	 * 
	 * @param properties
	 *            The data properties.
	 */
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Returns the ID of this feature.
	 * 
	 * @return The ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the ID of this feature.
	 * 
	 * @param id
	 *            The ID.
	 */
	public void setId(String id) {
		this.id = id;
	}

}

package de.fhpotsdam.unfolding.data;

import java.util.HashMap;

/**
 * A feature stores one or more locations, its type, and additional data properties.
 */
public class Feature {

	public static enum FeatureType {
		POINT, LINES, POLYGON
	}

	private FeatureType type;

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

	public FeatureType getType() {
		return type;
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public Object getProperty(String key) {
		return properties.get(key);
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

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
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

}

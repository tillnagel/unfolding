package de.fhpotsdam.unfolding.data;

import java.util.HashMap;

public class Feature {

	public static enum FeatureType {
		POINT, LINES, POLYGON
	}

	private FeatureType type;

	public HashMap<String, Object> properties = new HashMap<String, Object>();

	public Feature(FeatureType type) {
		this.type = type;
	}

	public FeatureType getType() {
		return type;
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}
	
	public void putProperty(String key, Object value) {
		properties.put(key, value);
	}

}

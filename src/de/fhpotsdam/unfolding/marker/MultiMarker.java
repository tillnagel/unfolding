package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;

/**
 * A MultiMarker enables handling of multiple, logically grouped markers. Properties and display states are the same for
 * all its markers.
 * 
 * A MultiMarker can consist of various sub-markers, even of different types. For instance, a MultiMarker could have
 * three polygon marker and one point marker.
 */
public class MultiMarker implements Marker {

	protected List<Marker> markers = new ArrayList<Marker>();
	public HashMap<String, Object> properties;
	protected boolean selected;
	protected boolean hidden;
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public void addMarkers(Marker... markers) {
		for (Marker marker : markers) {
			this.markers.add(marker);
		}
	}

	/**
	 * Return center of all markers.
	 * 
	 * 
	 * This uses marker.getLocation() which either returns single location, or centroid location (of shape marker), and
	 * then combines it. TODO Check whether to use {@link GeoUtils#getCentroid(List)} instead.
	 */
	@Override
	public Location getLocation() {
		Location center = new Location(0, 0);
		for (Marker marker : markers) {
			center.add(marker.getLocation());
		}
		center.div((float) markers.size());
		return center;
	}

	@Override
	public void setLocation(float lat, float lng) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocation(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * return distance between location and the (to the location) closest marker
	 */
	// REVISIT alternatively method could return distance to the of all markers
	// implement both in different methods? examples needed!
	@Override
	public double getDistanceTo(Location location) {
		double minDistance = Double.MAX_VALUE;
		for (Marker marker : markers) {
			double dist = marker.getDistanceTo(location);
			minDistance = dist < minDistance ? dist : minDistance;
		}
		return minDistance;
	}

	@Override
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public Object setProperty(String key, Object value) {
		return properties.put(key, value);
	}

	@Override
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	@Override
	public Object getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public String getStringProperty(String key) {
		Object value = properties.get(key);
		if (value != null && value instanceof String) {
			return (String) value;
		} else {
			return null;
		}
	}

	@Override
	public Integer getIntegerProperty(String key) {
		Object value = properties.get(key);
		if (value != null && value instanceof Integer) {
			return (Integer) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns true if at least one marker is hit.
	 */
	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		boolean inside = false;

		for (Marker marker : markers) {
			inside |= marker.isInside(map, checkX, checkY);
		}

		return inside;
	}

	/**
	 * Returns true if at least one marker is hit. Checks only AbstractShapeMarkers, as only those implement
	 * {@link AbstractShapeMarker#isInsideByLocation(float, float)}.
	 */
	public boolean isInsideByLocation(float latitude, float longitude) {
		boolean inside = false;

		for (Marker marker : markers) {
			if (marker instanceof MultiMarker) {
				MultiMarker multiMarker = (MultiMarker) marker;
				inside |= multiMarker.isInsideByLocation(latitude, longitude);
			} else if (marker instanceof AbstractShapeMarker) {
				AbstractShapeMarker shapeMarker = (AbstractShapeMarker) marker;
				inside |= shapeMarker.isInsideByLocation(latitude, longitude);
			}
		}

		return inside;
	}

	@Override
	public void draw(UnfoldingMap map) {
		for (Marker marker : markers) {
			marker.draw(map);
		}
	}

	/**
	 * Sets the selected status of all its markers.
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;

		for (Marker marker : markers) {
			marker.setSelected(selected);
		}
	}

	/**
	 * Indicates whether this multi marker is selected. This does not necessarily reflect the selected states of all its
	 * markers (i.e. a marker of a MultiMarker can have a different selection status):
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;

		for (Marker marker : markers) {
			marker.setHidden(hidden);
		}
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	public void setColor(int color) {
		for (Marker marker : markers) {
			marker.setColor(color);
		}
	}

	@Override
	public void setStrokeColor(int color) {
		for (Marker marker : markers) {
			marker.setStrokeColor(color);
		}
	}

	@Override
	public void setStrokeWeight(int weight) {
		for (Marker marker : markers) {
			marker.setStrokeWeight(weight);
		}
	}

	@Override
	public void setHighlightColor(int color) {
		for (Marker marker : markers) {
			marker.setHighlightColor(color);
		}
	}
	
	@Override
	public void setHighlightStrokeColor(int color) {
		for (Marker marker : markers) {
			marker.setHighlightStrokeColor(color);
		}
	}

	
}

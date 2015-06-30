package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Manages markers of different types. Is always connected to one map (for location to screen coordinate conversion).
 */
public class MarkerManager<E extends Marker> {

	protected UnfoldingMap map;
	protected List<E> markers;
	protected boolean bEnableDrawing;

	/**
	 * Creates a MarkerManager with an empty markers list.
	 */
	public MarkerManager() {
		markers = new ArrayList<E>();
		bEnableDrawing = true;
	}

	/**
	 * Creates a MarkerManager with given markers.
	 * 
	 * @param markers
	 *            The markers to add.
	 */
	public MarkerManager(List<E> markers) {
		this();
		addMarkers(markers);
	}

	/**
	 * Set the map to use for conversion of geo-locations to screen positions for the markers.
	 * 
	 * @param map
	 *            The map.
	 */
	public void setMap(UnfoldingMap map) {
		this.map = map;
	}

	/**
	 * Sets the markers to manage.
	 * 
	 * @param markers
	 *            A list of markers. If null all existing markers will be removed.
	 */
	public void setMarkers(List<E> markers) {
		if (markers != null) {
			this.markers = markers;
		} else {
			// Convenient method. Users should use clearMarkers() directly.
			clearMarkers();
		}
	}

	/**
	 * Removes a marker from the managed markers.
	 * 
	 * @param marker
	 *            The marker to remove.
	 * @return Whether the list contained the given marker.
	 */
	public boolean removeMarker(E marker) {
		return markers.remove(marker);
	}

	/**
	 * Removes all markers.
	 */
	public void clearMarkers() {
		markers.clear();
	}

	public boolean isDrawingEnabled() {
		return bEnableDrawing;
	}

	public void enableDrawing() {
		bEnableDrawing = true;
	}

	public void disableDrawing() {
		bEnableDrawing = false;
	}

	/**
	 * Toggles whether this marker manager draws all markers or none.
	 */
	public void toggleDrawing() {
		bEnableDrawing = !bEnableDrawing;
	}

	/**
	 * Adds a marker to the manager marker list.
	 * 
	 * @param marker
	 *            The marker to add.
	 * @return Whether the marker was added or not.
	 */
	public boolean addMarker(E marker) {
		if (markers == null) {
			this.markers = new ArrayList<E>();
		}

		if (markers.contains(marker))
			return false;

		markers.add(marker);
		return true;
	}

	/**
	 * Adds a list of markers to the managed markers.
	 * 
	 * @param markers
	 *            A list of markers.
	 */
	public void addMarkers(List<E> markers) {
		if (this.markers == null) {
			this.markers = new ArrayList<E>();
		}
		// TODO Only add marker if list does not contain it yet
		this.markers.addAll(markers);
	}

	/**
	 * Searches marker with ID.
	 * 
	 * @param id
	 *            The ID of the marker to find.
	 * @return The found Marker or null if not found.
	 */
	public E findMarkerById(String id) {
		E foundMarker = null;
		for (E marker : this.markers) {
			if (marker.getId().equals(id)) {
				foundMarker = marker;
			}
		}
		return foundMarker;
	}

	/**
	 * Searches list of markers with IDs.
	 * 
	 * @param ids
	 *            The list of IDs of the markers to find.
	 * @return The found Markers or an empty list if none found.
	 */
	public List<E> findMarkersByIds(List<String> ids) {
		List<E> foundMarkers = new ArrayList<E>();
		for (E marker : markers) {
			if (ids.contains(marker.getId())) {
				foundMarkers.add(marker);
			}
		}
		return foundMarkers;
	}

	/**
	 * Searches marker with a specific property.
	 * 
	 * @param key
	 *            The property key to search for.
	 * @param value
	 *            The property value to search for.
	 * @return The first found Marker or null if not found.
	 */
	public E findMarkerByProperty(String key, Object value) {
		E foundMarker = null;
		for (E marker : this.markers) {
			Object foundValue = marker.getProperty(key);
			if (foundValue != null && foundValue.equals(value)) {
				foundMarker = marker;
			}
		}
		return foundMarker;
	}

	public E findMarkerByName(String name) {
		return findMarkerByProperty("Name", name);
	}

	/**
	 * Returns all markers managed by this MarkerManager.
	 * 
	 * @return A list of markers.
	 */
	public List<E> getMarkers() {
		return markers;
	}

	/**
	 * @deprecated Replaced by {@link #getFirstHitMarker(float, float)}
	 */
	@Deprecated
	public Marker isInside(float checkX, float checkY) {
		return getFirstHitMarker(checkX, checkY);
	}

	/**
	 * Returns the nearest marker to the given screen coordinates.
	 * 
	 * @param checkX
	 *            The x position to check.
	 * @param checkY
	 *            The y position to check.
	 * @return The nearest marker, or null if no marker was hit.
	 */
	public E getNearestMarker(float checkX, float checkY) {
		E foundMarker = null;
		double minDist = Double.MAX_VALUE;
		for (E marker : markers) {
			// Distance between marker geo-location and check position in geo-location
			double dist = marker.getDistanceTo(map.getLocation(checkX, checkY));
			if (minDist == dist) {
				if (marker.isInside(map, checkX, checkY)) {
					foundMarker = marker;
				}
			} else if (minDist > dist) {
				minDist = dist;
				foundMarker = marker;
			}
		}
		return foundMarker;
	}

	/**
	 * Returns the first marker which the given screen coordinates hit.
	 * 
	 * @param checkX
	 *            The x position to check.
	 * @param checkY
	 *            The y position to check.
	 * @return The hit marker, or null if no marker was hit.
	 */
	public E getFirstHitMarker(float checkX, float checkY) {

		E foundMarker = null;
		// NB: Markers should be ordered, e.g. by size ascending, i.e. big, medium, small
		for (E marker : markers) {

			// NB: If markers are order by size descending, i.e. small, medium, big
			// for (int i = markers.size() - 1; i >= 0; i--) {
			// Marker marker = markers.get(i);

			if (marker.isInside(map, checkX, checkY)) {
				foundMarker = marker;
				break;
			}
		}
		return foundMarker;
	}

	/**
	 * Returns all hit markers.
	 * 
	 * @param checkX
	 *            The x position to check.
	 * @param checkY
	 *            The y position to check.
	 * @return All hit markers, or an empty list if no marker was hit.
	 */
	public List<E> getHitMarkers(float checkX, float checkY) {
		List<E> hitMarkers = new ArrayList<E>();
		for (E marker : markers) {
			if (marker.isInside(map, checkX, checkY)) {
				hitMarkers.add(marker);
			}
		}
		return hitMarkers;
	}

	/**
	 * Internal method to draw all managed markers.
	 */
	public void draw() {
		if (!bEnableDrawing)
			return;

		for (Marker marker : markers) {
			marker.draw(map);
		}
	}

}

package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;

/*
 * Manages markers of different types. Is always connected to one map (for location to screen coordinate conversion).
 * 
 */
public class MarkerManager<E extends Marker> {

	public static Logger log = Logger.getLogger(MarkerManager.class);

	Map map;
	List<E> markers;

	public MarkerManager(Map map) {
		this.map = map;
	}

	public MarkerManager(Map map, List<E> markers) {
		this.map = map;
		addMarkers(markers);
	}

	public void setMarkers(List<E> markers) {
		this.markers = markers;
	}

	public boolean addMarker(E marker) {
		if (markers == null) {
			this.markers = new ArrayList<E>();
		}

		if (markers.contains(marker))
			return false;

		markers.add(marker);
		return true;
	}

	public void addMarkers(List<E> markers) {
		if (this.markers == null) {
			this.markers = new ArrayList<E>();
		}
		this.markers.addAll(markers);
	}

	public List<? extends Marker> getMarkers() {
		return markers;
	}

	/**
	 * @deprecated Replaced by {@link #getFirstHitMarker(float, float)}
	 */
	@Deprecated
	public Marker isInside(float checkX, float checkY) {
		return getFirstHitMarker(checkX, checkY);
	}

	public Marker getFirstHitMarker(float checkX, float checkY) {
		Marker foundMarker = null;
		// NB: Markers are ordered by size ascending, i.e. big, medium, small
		for (Marker marker : markers) {

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

	public List<Marker> getHitMarkers(float checkX, float checkY) {
		List<Marker> hitMarkers = new ArrayList<Marker>();
		for (Marker marker : markers) {
			if (marker.isInside(map, checkX, checkY)) {
				hitMarkers.add(marker);
			}
		}
		return hitMarkers;
	}

	public void draw() {
		// REVISIT Why twice? Here and in drawOuter()?
		// To allow both; markers either implement one or the other.
		// Off-map cut-off depends on it.

		for (Marker marker : markers) {
			marker.draw(map);
		}
	}

	public void drawOuter() {
		for (Marker marker : markers) {
			marker.drawOuter(map);
		}
	}

}

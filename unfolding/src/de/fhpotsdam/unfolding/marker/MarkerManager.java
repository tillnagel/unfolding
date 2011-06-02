package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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

		this.markers = markers;
	}

	public void setMarkers(List<E> markers) {
		this.markers = markers;
	}

	public boolean addMarker(E marker) {
		if (markers == null) {
			markers = new ArrayList<E>();
		}

		if (markers.contains(marker))
			return false;

		markers.add(marker);
		return true;
	}

	public void addMarkers(List<E> markers) {
		this.markers.addAll(markers);
	}

	public List<? extends Marker> getMarkers() {
		return markers;
	}

	public Marker isInside(float checkX, float checkY) {
		Marker foundMarker = null;
		for (Marker marker : markers) {
			if (marker.isInside(map, checkX, checkY)) {
				foundMarker = marker;
				break;
			}
		}
		return foundMarker;
	}

	public void draw() {
		// REVISIT Why twice? Here and in drawOuter()?

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

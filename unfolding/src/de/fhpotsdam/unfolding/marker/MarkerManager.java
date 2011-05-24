package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.Map;

/*
 * Manages marker.
 * 
 * TODO If app wants to manage different marker subclasses with same MarkerManager, all lists have to be
 * Marker only (not subclass-typed) 
 */
public class MarkerManager {

	public static Logger log = Logger.getLogger(MarkerManager.class);

	Map map;
	List<Marker> markers;

	public MarkerManager(Map map) {
		this.map = map;
	}
	public MarkerManager(Map map, Marker... markers) {
		this(map, Arrays.asList(markers));
	}

	public MarkerManager(Map map, List<Marker> markers) {
		this.map = map;

		this.markers = markers;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public void addMarker(Marker marker) {
		if (markers == null) {
			markers = new ArrayList<Marker>();
		}
		markers.add(marker);
	}
	public void addMarkers(List<Marker> markers) {
		this.markers.addAll(markers);
	}

	public List<Marker> getMarkers() {
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

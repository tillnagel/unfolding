package de.fhpotsdam.generics;

import java.util.List;

import de.fhpotsdam.unfolding.marker.Marker;

public class MarkerManager<E extends Marker> {

	protected List<E> markers;
	
	public void addMarker(E marker) {
		markers.add(marker);
	}

	public List<E> getMarkers() {
		return markers;
	}
	
}

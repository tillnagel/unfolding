package de.fhpotsdam.unfolding.data;

import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;

public class SimpleMarkerFactory extends MarkerFactory {

	protected Marker createPolygonMarker(MultiFeature feature) {
		return new SimplePolygonMarker(feature.getLocations());
	}

	protected Marker createPointMarker(PointFeature feature) {
		return new SimpleMarker(feature.getLocation());
	}

	protected Marker createLinesMarker(MultiFeature feature) {
		return new SimpleLinesMarker(feature.getLocations());
	}

}

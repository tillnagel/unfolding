package de.fhpotsdam.unfolding.examples.marker.advanced;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Advanced example to demonstrate custom nearest marker test.
 * 
 * getNearestMarker(x,y) returns the closest marker to the given screen position. Using
 * DistancePerLocationPolygonMarker, i.e. instead of the default implementation of using the centroid of the polygon,
 * this tests all vertices of the polygon.
 */
@SuppressWarnings("serial")
public class GetNearestMultiMarkerApp extends PApplet {

	UnfoldingMap map;
	List<Marker> countryMarkers = new ArrayList<Marker>();

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomToLevel(2);
		map.panTo(new Location(58.631217f, -101.601562f));
		MapUtils.createDefaultEventDispatcher(this, map);

		initPolygons();
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void mouseMoved() {
		Marker marker = map.getDefaultMarkerManager().getNearestMarker(mouseX, mouseY);
		if (marker != null) {
			for (Marker countryMarker : countryMarkers) {
				countryMarker.setSelected(countryMarker == marker);
			}
		}
	}

	private void initPolygons() {
		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		List<Feature> selectedCountries = new ArrayList<Feature>();
		for (Feature feature : countries) {
			if (feature.getId().equalsIgnoreCase("CAN") || feature.getId().equalsIgnoreCase("USA")) {
				selectedCountries.add(feature);
			}
		}
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(DistancePerLocationPolygonMarker.class);
		countryMarkers = markerFactory.createMarkers(selectedCountries);
		map.addMarkers(countryMarkers);
	}

}

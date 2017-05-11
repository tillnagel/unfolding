package de.fhpotsdam.unfolding.examples.marker.advanced.centroid;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/**
 * Example using own marker class to create markers from features via the MarkerFactory.
 * 
 * The markers display labels at the center of the polygons, additionally to the polygons (by using
 * SimplePolygonMarker's draw method) themselves.
 */
public class CentroidMarkerApp extends PApplet {

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");

		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(CentroidLabelMarker.class);
		List<Marker> countryMarkers = markerFactory.createMarkers(countries);

		map.addMarkers(countryMarkers);
	}

	public void draw() {
		background(160);
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { CentroidMarkerApp.class.getName() });
	}

}

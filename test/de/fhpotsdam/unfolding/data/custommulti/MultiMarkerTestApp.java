package de.fhpotsdam.unfolding.data.custommulti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MultiMarkerTestApp extends PApplet {

	UnfoldingMap map;
	String[] ids = { "NLD", "FRA", "IRL", "AUT", "GBR" };
	List<String> specialIDs = new ArrayList<String>(Arrays.asList(ids));

	public void setup() {
		size(800, 600, P2D);

		List<Feature> countryFeatures = GeoJSONReader.loadData(this, "data/countries.geo.json");
		// List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countryFeatures);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(5, new Location(48, 5));

		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setMultiClass(ConvexHullMultiMarker.class);
		List<Marker> countryMarkers = markerFactory.createMarkers(countryFeatures);
		map.addMarkers(filterMarkers(countryMarkers));
	}

	public void draw() {
		map.draw();
	}

	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}
	}

	public List<Marker> filterMarkers(List<Marker> markers) {
		List<Marker> filteredMarkers = new ArrayList<Marker>();
		for (Marker marker : markers) {
			if (specialIDs.contains(marker.getId())) {
				filteredMarkers.add(marker);
			}
		}
		return filteredMarkers;
	}

}

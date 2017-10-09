package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Interactive vector tiles example to dynamically load and display buildings from OpenStreetMap.
 * 
 * Click on the map to load its vector tiles. Press SPACE to clear markers. Press 'A' to load all vector tiles for the
 * current map view.
 * 
 * Handles overlapping features, i.e. features returned in multiple vector tiles are shown only once if the ID is the
 * same. This mechanism does not take into account the zoom factor, i.e. the same feature is not loaded anew for another
 * zoom level.
 * 
 */
public class VectorTilesApp extends PApplet {
	
	public static final String MAPZEN_API_KEY = "YOUR_API_KEY";
	
	UnfoldingMap map;
	DebugDisplay debugDisplay;

	VectorTilesUtils vectorTilesUtils;
	/** Name of the features layer (in OpenStreetMap). */
	String buildingsLayer = "buildings";

	boolean loadUniqueMarkers = true;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { VectorTilesApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);

		vectorTilesUtils = new VectorTilesUtils(this, map, MAPZEN_API_KEY);

		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(buildingsLayer, width / 2, height / 2);
		map.addMarkers(markers);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}

	public void mouseClicked() {
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(buildingsLayer, mouseX, mouseY);
		addMarkers(markers, loadUniqueMarkers);
	}

	public void keyPressed() {
		if (key == 'u') {
			loadUniqueMarkers = !loadUniqueMarkers;
		}
		if (key == ' ') {
			map.getDefaultMarkerManager().clearMarkers();
		}
		if (key == 'a') {
			List<Marker> markers = vectorTilesUtils.loadMarkersForCurrentMapView(buildingsLayer);
			addMarkers(markers, loadUniqueMarkers);
		}
	}

	/*
	 * @param unique Indicates whether to check for same markers only loaded. If true only new markers are returned, if
	 * false all markers containing possible duplicates.
	 */
	public void addMarkers(List<Marker> markers, boolean unique) {
		if (unique) {
			// Add only new markers
			addUniqueMarkers(markers);
		} else {
			// Add all markers
			map.addMarkers(markers);
		}
	}

	// TODO Move addUniqueMarkers to UnfoldingMap.
	public void addUniqueMarkers(List<Marker> markers) {
		MarkerManager<Marker> markerManager = map.getDefaultMarkerManager();
		int newMarkerCount = 0;
		int oldMarkerCount = 0;
		for (Marker marker : markers) {
			if (markerManager.findMarkerById(marker.getId()) == null) {
				markerManager.addMarker(marker);
				newMarkerCount++;
			} else {
				oldMarkerCount++;
			}
		}
		println("Added " + newMarkerCount + " new markers, and omitted " + oldMarkerCount
				+ " previously loaded markers.");
	}

}

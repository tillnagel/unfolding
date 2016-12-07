package de.fhpotsdam.unfolding.examples.marker.dynamic;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example for dynamic marker handling. This adds, removes and clears markers of a single MarkerManager.
 * 
 * Press 'r' to remove the Berlin marker, 'a' to re-add it, and 'c' to clear all markers.
 */
@SuppressWarnings("serial")
public class DynamicMarkerApp extends PApplet {

	UnfoldingMap map;

	MarkerManager<Marker> markerManager;
	SimplePointMarker berlinMarker;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { DynamicMarkerApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this);

		map.zoomToLevel(3);
		map.panTo(new Location(40f, -42f));
		MapUtils.createDefaultEventDispatcher(this, map);

		// Get default MarkerManager (still empty at this moment)
		markerManager = map.getDefaultMarkerManager();

		// Create Markers from Locations
		Location berlinLocation = new Location(52.5f, 13.4f);
		Location mexicoCityLocation = new Location(19.4f, -99.1f);

		// Point Markers
		berlinMarker = new SimplePointMarker(berlinLocation);
		SimplePointMarker mexicoCityMarker = new SimplePointMarker(mexicoCityLocation);
		// Line Marker
		SimpleLinesMarker connectionMarker = new SimpleLinesMarker(berlinLocation, mexicoCityLocation);

		markerManager.addMarker(berlinMarker);
		markerManager.addMarker(mexicoCityMarker);
		markerManager.addMarker(connectionMarker);
	}

	public void draw() {
		background(240);

		// Drawing Markers in handled internally
		map.draw();
	}

	public void keyPressed() {
		if (key == 'r') {
			markerManager.removeMarker(berlinMarker);
		}
		if (key == 'a') {
			markerManager.addMarker(berlinMarker);
		}
		if (key == 'c') {
			markerManager.clearMarkers();
		}
	}

}

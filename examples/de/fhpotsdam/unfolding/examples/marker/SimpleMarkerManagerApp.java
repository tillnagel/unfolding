package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example with three markers, managed by the MarkerManager. Displays two point markers, and one line marker.
 * 
 * Managing and drawing the markers is handled internally, with all markers cut-off at the border of the map.
 * Unfolding's simple marker (SimpleMarker, SimpleLinesMarker, and SimplePolygonMarker) provide some styling
 * functionality. For more customization you need to create your own Marker classes.
 */
@SuppressWarnings("serial")
public class SimpleMarkerManagerApp extends PApplet {

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { SimpleMarkerManagerApp.class.getName() });
	}

	public void setup() {

		map = new UnfoldingMap(this, new Google.GoogleMapProvider());

		map.zoomToLevel(3);
		map.panTo(new Location(40f, -42f));
		MapUtils.createDefaultEventDispatcher(this, map);

		// Create Markers from Locations
		Location berlinLocation = new Location(52.5f, 13.4f);
		Location mexicoCityLocation = new Location(19.4f, -99.1f);

		// Point Markers
		SimplePointMarker berlinMarker = new SimplePointMarker(berlinLocation);
		SimplePointMarker mexicoCityMarker = new SimplePointMarker(mexicoCityLocation);
		// Line Marker
		SimpleLinesMarker connectionMarker = new SimpleLinesMarker(berlinLocation, mexicoCityLocation);

		// Add Markers to the maps default MarkerManager
		map.addMarkers(berlinMarker, mexicoCityMarker, connectionMarker);

		// Adapt style
		berlinMarker.setColor(color(255, 0, 0, 100));
		berlinMarker.setStrokeColor(color(255, 0, 0));
		berlinMarker.setStrokeWeight(2);
	}

	public void draw() {
		background(240);

		// Drawing Markers in handled internally
		map.draw();
	}

}

package de.fhpotsdam.unfolding.examples.marker.connectionmarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Creates random markers, and checks whether they are in vicinity to each other. If they are neighbors a connection is
 * shown.
 */
@SuppressWarnings("serial")
public class NeighborMarkersApp extends PApplet {

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { NeighborMarkersApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomToLevel(11);
		map.panTo(new Location(52.53f, 13.4f));
		MapUtils.createDefaultEventDispatcher(this, map);

		initMarkers();
	}

	public void initMarkers() {
		map.getDefaultMarkerManager().clearMarkers();

		// Create Markers from random Locations
		List<Marker> markers = new ArrayList<Marker>();
		List<Marker> connectionMarkers = new ArrayList<Marker>();

		for (int i = 0; i < 30; i++) {
			markers.add(new SimplePointMarker(new Location(random(52.46f, 52.61f), random(13.23f, 13.54f))));
		}

		// Create connections between near-by markers
		for (Marker marker : markers) {
			for (Marker markerTo : markers) {
				// Less than 3 km
				if (GeoUtils.getDistance(marker.getLocation(), markerTo.getLocation()) < 3) {
					ConnectionMarker cm = new ConnectionMarker(marker, markerTo);
					connectionMarkers.add(cm);
				}
			}
		}

		// Add Markers to the maps default MarkerManager
		map.addMarkers(markers);
		map.addMarkers(connectionMarkers);
	}

	public void draw() {
		background(240);

		// Drawing Markers in handled internally
		map.draw();
	}

	public void keyPressed() {
		if (key == ' ') {
			initMarkers();
		}
	}

}

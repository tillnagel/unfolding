package de.fhpotsdam.unfolding.examples.marker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Creates random markers, and checks whether they are in vicinity. If they are neighbors a
 * connection is shown.
 */
public class NeighborMarkersApp extends PApplet {

	Map map;

	List<Marker> markers = new ArrayList<Marker>();
	List<Marker> connectionMarkers = new ArrayList<Marker>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, 0, 0, 800, 500);
		map.zoomToLevel(2);
		map.panTo(new Location(40f, 20f));
		MapUtils.createDefaultEventDispatcher(this, map);

		// Create markers
		for (int i = 0; i < 30; i++) {
			NeighborMarker m = new NeighborMarker();
			m.location = new Location(random(52.463f, 52.608f), random(13.23f, 13.54f));
			markers.add(m);
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

		MarkerManager markerManager = new MarkerManager(map, markers);
		markerManager.addMarkers(connectionMarkers);
		map.mapDisplay.setMarkerManager(markerManager);
	}

	public void draw() {
		background(240);

		map.draw();
	}

}

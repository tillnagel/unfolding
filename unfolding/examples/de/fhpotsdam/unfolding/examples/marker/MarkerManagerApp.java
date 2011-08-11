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
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example with two markers, managed by the MarkerManager. The markers are cut-off at the
 * border of the map.
 */
public class MarkerManagerApp extends PApplet {

	Map map;

	List<Marker> markers = new ArrayList<Marker>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		
		println("test");

		map = new Map(this, "map", 50, 50, 700, 500, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map.zoomToLevel(2);
		map.panTo(new Location(40f, 20f));
		MapUtils.createDefaultEventDispatcher(this, map);

		markers = createMarkers();

		MarkerManager markerManager = new MarkerManager(map, markers);
		map.mapDisplay.setMarkerManager(markerManager);
	}

	public void draw() {
		background(240);
		map.draw();
	}

	private List<Marker> createMarkers() {
		List<Marker> markers = new ArrayList<Marker>();

		PlaceMarker marker = new PlaceMarker();
		// Berlin
		marker.location = new Location(52.5f, 13.4f);
		markers.add(marker);

		PlaceMarker marker2 = new PlaceMarker();
		// Mexiko City
		marker2.location = new Location(19.4f, -99.1f);
		markers.add(marker2);

		return markers;
	}
}

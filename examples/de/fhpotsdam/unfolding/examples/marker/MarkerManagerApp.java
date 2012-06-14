package de.fhpotsdam.unfolding.examples.marker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays own marker on a map. 
 */
public class MarkerManagerApp extends PApplet {

	Map map;

	Location berlinLocation = new Location(52.5f, 13.4f);
	Location mexicoCityLocation = new Location(19.4f, -99.1f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(2);
		map.panTo(new Location(40f, 20f));
		MapUtils.createDefaultEventDispatcher(this, map);

		map.addMarkers(createMarkers());
	}

	public void draw() {
		background(240);
		map.draw();
	}

	private List<Marker> createMarkers() {
		List<Marker> markers = new ArrayList<Marker>();

		DrawOuterMarker marker = new DrawOuterMarker(berlinLocation);
		markers.add(marker);

		DrawOuterMarker marker2 = new DrawOuterMarker(mexicoCityLocation);
		markers.add(marker2);

		return markers;
	}
}

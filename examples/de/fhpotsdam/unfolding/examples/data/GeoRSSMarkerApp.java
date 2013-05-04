package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays earthquake markers from an RSS feed.
 */
public class GeoRSSMarkerApp extends PApplet {

	String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml";

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		map = new UnfoldingMap(this, 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, earthquakesURL);
		List<Marker> markers = MapUtils.createSimpleMarkers(features);
		map.addMarkers(markers);
	}

	public void draw() {
		background(0);
		map.draw();
	}

}

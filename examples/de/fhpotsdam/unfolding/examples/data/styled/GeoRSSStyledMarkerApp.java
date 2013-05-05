package de.fhpotsdam.unfolding.examples.data.styled;

import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays earthquake markers from an RSS feed, but with own markers.
 * 
 * Uses MarkerFactory (as in the default marker creation way), but uses own styled EarthquakeMarker.
 */
public class GeoRSSStyledMarkerApp extends PApplet {

	String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml";

	UnfoldingMap map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, earthquakesURL);
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPointClass(EarthquakeMarker.class);
		List<Marker> markers = markerFactory.createMarkers(features);
		map.addMarkers(markers);
	}

	public void draw() {
		map.draw();
	}

}

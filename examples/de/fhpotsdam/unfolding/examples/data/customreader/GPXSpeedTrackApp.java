package de.fhpotsdam.unfolding.examples.data.customreader;

import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Loads a GPX file containing a bike tour in Berlin, and displays each segment with speed mapped to color.
 * 
 * This example shows a more complex usage of own data loading and special markers.
 * See @{link GPXSpeedReader} for how to create your own data reader creating geo-data Features.
 */
public class GPXSpeedTrackApp extends PApplet {

	UnfoldingMap map;

	Location startLocation = new Location(52.49f, 13.44f);
	
	public void settings() {
		size(800, 600, P2D);
	}
	
	public void setup() {
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(14, startLocation);

		List<Feature> features = GPXSpeedReader.loadData(this, "data/bike-tour.gpx");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setLineClass(ColoredLinesMarker.class);
		List<Marker> markers = markerFactory.createMarkers(features);
		map.addMarkers(markers);
	}

	public void draw() {
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { GPXSpeedTrackApp.class.getName() });
	}
}
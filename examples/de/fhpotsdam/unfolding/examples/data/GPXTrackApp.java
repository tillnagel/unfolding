package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GPXReader;
import de.fhpotsdam.unfolding.examples.data.customreader.GPXSpeedTrackApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays a track loaded from a GPX file containing a bike tour in Berlin.
 * 
 * See {@link GPXSpeedTrackApp} for custom GPX parsing and marker display.
 */
public class GPXTrackApp extends PApplet {

	UnfoldingMap map;

	Location startLocation = new Location(52.49f, 13.44f);

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(13, startLocation);

		List<Feature> features = GPXReader.loadData(this, "data/bike-tour.gpx");
		List<Marker> markers = MapUtils.createSimpleMarkers(features);
		map.addMarkers(markers);
	}

	public void draw() {
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { GPXTrackApp.class.getName() });
	}
}
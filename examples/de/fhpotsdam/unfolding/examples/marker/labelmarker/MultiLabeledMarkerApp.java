package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays markers for news items, read from an (archived) BBC RSS stream. Highlights one of the markers and shows its
 * label when user hovers over the mouse. Selection works out-of-the-box, but for using the 'title' property as label a
 * special method has to be called.
 * 
 * The highlight check is done manually for all markers in mouseMoved().
 * 
 * If you want to prevent label overlapping, you either need to re-sort the markers, or create two marker sets, one for
 * the dots, and one for the labels.
 * 
 */
public class MultiLabeledMarkerApp extends PApplet {

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MultiLabeledMarkerApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load from GeoRSS file
		List<Feature> features = GeoRSSReader.loadData(this, "data/bbc-georss-test.xml");
		// Create (display) markers from (data) features
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPointClass(LabeledMarker.class);
		List<Marker> markers = markerFactory.createMarkers(features);
		// Add markers to map
		map.addMarkers(markers);

		// Use property 'title' as label
		populateMarkerLabels(markers);
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void mouseMoved() {
		// Deselect all marker
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}

		// Select hit marker
		// Note: Use getHitMarkers(x, y) if you want to allow multiple selection.
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}
	}

	private void populateMarkerLabels(List<Marker> markers) {
		// TODO Add convenience method: markerFactory.addPropertyRule("title", "name");
		// See MarkerFactory

		for (Marker marker : markers) {
			LabeledMarker labeledMarker = (LabeledMarker) marker;
			labeledMarker.name = marker.getStringProperty("title");
		}
	}

}

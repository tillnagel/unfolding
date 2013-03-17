package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays markers for news items, read from an (archived) BBC RSS stream.
 * 
 * Shows news titles as labels. Hover over a marker to show labels.
 * 
 * This example creates own markers from the data features. If you want to prevent label overlapping, you either need to
 * re-sort the markers, or create two marker sets, one for the dots, and one for the labels.
 * 
 * TODO till: Create example with two markers for same item (dot plus label)
 * 
 */
public class MultiLabeledMarkerApp extends PApplet {

	UnfoldingMap map;Location berlinLocation = new Location(52.5f, 13.4f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(2);
		//map.panTo(berlinLocation);
		MapUtils.createDefaultEventDispatcher(this, map);

//		List<Feature> features = GeoRSSReader.loadData(this, "bbc-georss-test.xml");
//		List<Marker> markers = createLabeledMarkers(features);
//		map.addMarkers(markers);
//		
		font = loadFont("Helvetica-16.vlw");
		Marker berlinMarker = new LabeledMarker(new Location(52.5f, 13.4f), "Fossils", font, 15);
		map.addMarkers(berlinMarker);
	}

	PFont font;
	
	public void draw() {
		background(240);
		map.draw();
		
		textFont(font);
		text("Fossils", 100, 100);
	}

	public void mouseMoved() {
		MarkerManager<? extends Marker> mm = map.mapDisplay.getLastMarkerManager();

		// Deselect all marker
		for (Marker marker : mm.getMarkers()) {
			marker.setSelected(false);
		}

		// Select hit marker
		Marker marker = mm.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}
	}

	public List<Marker> createLabeledMarkers(List<Feature> features) {
		PFont font = loadFont("Helvetica-16.vlw");
		List<Marker> markers = new ArrayList<Marker>();
		for (Feature feature : features) {
			String label = feature.getStringProperty("title");
			PointFeature pointFeature = (PointFeature) feature;
			Marker marker = new LabeledMarker(pointFeature.getLocation(), label, font, 15);
			markers.add(marker);
		}
		return markers;
	}
}

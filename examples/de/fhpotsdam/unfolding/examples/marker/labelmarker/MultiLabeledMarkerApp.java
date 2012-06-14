package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays markers for news items, read from an (archived) BBC RSS stream.
 * 
 * Shows news titles as labels on mouse over. Creates own markers from the data features.
 * 
 */
public class MultiLabeledMarkerApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		PFont font = loadFont("Miso-Light-12.vlw");

		map = new Map(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, "bbc-georss-test.xml");
		List<Marker> markers = createLabeledMarkers(font, features);
		map.addMarkers(markers);
	}

	public void draw() {
		background(240);
		map.draw();
	}

	
	public void mouseMoved() {
		MarkerManager<Marker> mm = map.mapDisplay.getLastMarkerManager();

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

	public static List<Marker> createLabeledMarkers(PFont font, List<Feature> features) {
		List<Marker> markers = new ArrayList<Marker>();
		for (Feature feature : features) {
			String label = feature.getStringProperty("title");
			PointFeature pointFeature = (PointFeature) feature;
			Marker marker = new LabeledMarker(font, label, pointFeature.getLocation(), 20);
			markers.add(marker);
		}
		return markers;
	}
}

package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Creates custom markers from features, and manually populates label from a data property. Different to
 * {@link MultiLabeledMarkerApp}.
 */
public class ManualLabelMarkerApp extends PApplet {

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { ManualLabelMarkerApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, "data/bbc-georss-test.xml");
		List<Marker> markers = createLabeledMarkers(features);
		map.addMarkers(markers);
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
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		// NB: Use mm.getHitMarkers(x, y) for multi-selection.
		if (marker != null) {
			marker.setSelected(true);
		}
	}

	public List<Marker> createLabeledMarkers(List<Feature> features) {
		PFont font = loadFont("ui/OpenSans-12.vlw");
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

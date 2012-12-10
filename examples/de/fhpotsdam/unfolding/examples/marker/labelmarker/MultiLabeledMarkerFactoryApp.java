package de.fhpotsdam.unfolding.examples.marker.labelmarker;

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
 * Similar to MultiLabeldMarkerApp, but here the custom markers are created by the MarkerFactory.
 * 
 * Selection works out-of-the-box, but for using the 'title' property as label a special method has to be called. 
 * 
 */
public class MultiLabeledMarkerFactoryApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, "bbc-georss-test.xml");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPointClass(LabeledMarker.class);
		//TODO Add convenience method: markerFactory.addPropertyRule("title", "name");
		List<Marker> markers = markerFactory.createMarkers(features);
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
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		// NB: Use mm.getHitMarkers(x, y) for multi-selection.
		if (marker != null) {
			marker.setSelected(true);
		}
	}
	
	private void populateMarkerLabels(List<Marker> markers) {
		for (Marker marker : markers) {
			LabeledMarker labeledMarker = (LabeledMarker) marker;
			labeledMarker.name = marker.getStringProperty("title");
		}
	}

}

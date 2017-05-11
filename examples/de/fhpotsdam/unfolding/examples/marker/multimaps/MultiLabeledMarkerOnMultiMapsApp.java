package de.fhpotsdam.unfolding.examples.marker.multimaps;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.examples.marker.labelmarker.ManualLabelMarkerApp;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/**
 * Similar to {@link ManualLabelMarkerApp}, but marker appear on two maps.
 * 
 * Markers on both maps react to hover, but selection is always visible in both maps, as the markers are the same.
 */
public class MultiLabeledMarkerOnMultiMapsApp extends ManualLabelMarkerApp {

	UnfoldingMap map1;
	UnfoldingMap map2;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map1 = new UnfoldingMap(this, "map", 50, 50, 500, 500);
		map1.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map1);

		map2 = new UnfoldingMap(this, "map", 575, 50, 150, 150);
		MapUtils.createDefaultEventDispatcher(this, map2);

		List<Feature> features = GeoRSSReader.loadData(this, "data/bbc-georss-test.xml");
		List<Marker> markers = createLabeledMarkers(features);
		map1.addMarkers(markers);
		map2.addMarkers(markers);
	}

	public void draw() {
		background(240);
		map1.draw();
		map2.draw();
	}

	public void mouseMoved() {
		checkInsideMarker(map1);
		checkInsideMarker(map2);
	}

	public void checkInsideMarker(UnfoldingMap map) {
		if (map.isHit(mouseX, mouseY)) {
			// Deselect all marker
			for (Marker marker : map.getMarkers()) {
				marker.setSelected(false);
			}

			// Select hit marker
			Marker marker = map.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				marker.setSelected(true);
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MultiLabeledMarkerOnMultiMapsApp.class.getName() });
	}

}

package de.fhpotsdam.unfolding.examples.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays the subway lines of Boston, read from a GeoJSON file.
 * 
 * This example shows how to load data features and create markers manually in order to map specific properties; in this
 * case the colors according to the MBTA schema.
 */
public class TransitLinesMarkerApp extends PApplet {

	Location bostonLocation = new Location(42.357778f, -71.061667f);

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
		smooth();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { TransitLinesMarkerApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, new StamenMapProvider.TonerBackground());
		map.zoomToLevel(11);
		map.panTo(bostonLocation);
		map.setZoomRange(9, 17); // prevent zooming too far out
		map.setPanningRestriction(bostonLocation, 50);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> transitLines = GeoJSONReader.loadData(this, "data/MBTARapidTransitLines.json");

		// Create marker from features, and use LINE property to color the markers.
		List<Marker> transitMarkers = new ArrayList<Marker>();
		for (Feature feature : transitLines) {
			ShapeFeature lineFeature = (ShapeFeature) feature;

			SimpleLinesMarker m = new SimpleLinesMarker(lineFeature.getLocations());
			String lineColor = lineFeature.getStringProperty("LINE");
			int color = 0;
			// Original MBTA colors
			if (lineColor.equals("BLUE")) {
				color = color(44, 91, 167);
			}
			if (lineColor.equals("RED")) {
				color = color(233, 57, 35);
			}
			if (lineColor.equals("GREEN")) {
				color = color(59, 130, 79);
			}
			if (lineColor.equals("SILVER")) {
				color = color(154, 156, 157);
			}
			if (lineColor.equals("ORANGE")) {
				color = color(238, 137, 40);
			}
			m.setColor(color);
			m.setStrokeWeight(5);
			transitMarkers.add(m);
		}

		map.addMarkers(transitMarkers);
	}

	public void draw() {
		map.draw();
	}

}

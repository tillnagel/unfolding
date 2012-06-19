package de.fhpotsdam.unfolding.examples.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MultiFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays subway lines of Boston, read from a GeoJSON file.
 * 
 * This example shows how to load data features and create markers manually in order to map specific properties, in this
 * case the colors according to the MBTA schema.
 */
public class TransitLinesMarkerApp extends PApplet {

	Location bostonLocation = new Location(42.357778f, -71.061667f);

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new Map(this);
		map.zoomToLevel(11);
		map.panTo(bostonLocation);
		map.setZoomRange(9, 17); // prevent zooming too far out
		map.setPanningRestriction(bostonLocation, 50);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> transitLines = GeoJSONReader.loadData(this, "MBTARapidTransitLines.json");

		// Create marker from features, and use LINE property to color the markers.
		List<Marker> transitMarkers = new ArrayList<Marker>();
		for (Feature feature : transitLines) {
			MultiFeature lineFeature = (MultiFeature) feature;

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
			m.setStrokeWeight(2);
			transitMarkers.add(m);
		}

		map.addMarkers(transitMarkers);
	}

	public void draw() {
		background(160);
		map.draw();
	}

}

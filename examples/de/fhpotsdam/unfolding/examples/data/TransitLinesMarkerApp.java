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
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays subway lines of Boston, read from a GeoJSON file.
 * 
 * Press SPACE to toggle display of the lines.
 */
public class TransitLinesMarkerApp extends PApplet {

	Location bostonLocation = new Location(42.357778f, -71.061667f);

	Map map;

	MarkerManager<Marker> transitLinesManager;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new Map(this, 50, 50, 700, 500);
		map.zoomToLevel(11);
		map.panTo(bostonLocation);
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

		transitLinesManager = new MarkerManager<Marker>();
		transitLinesManager.setMarkers(transitMarkers);
		map.addMarkerManager(transitLinesManager);
	}

	public void draw() {
		background(160);
		map.draw();
	}

	public void keyPressed() {
		if (key == ' ') {
			transitLinesManager.toggleDrawing();
		}
	}

}

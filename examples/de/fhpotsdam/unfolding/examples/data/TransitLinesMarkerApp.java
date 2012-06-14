package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
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
		List<Marker> transitMarkers = MapUtils.createSimpleMarkers(transitLines);
		
		// TODO @felix: Why connect manager to map twice?
		transitLinesManager = new MarkerManager<Marker>(map);
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

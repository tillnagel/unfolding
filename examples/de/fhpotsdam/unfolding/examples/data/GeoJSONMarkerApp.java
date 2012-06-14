package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays earthquake markers from an RSS feed.
 */
public class GeoJSONMarkerApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new Map(this, 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Marker> markers = MapUtils.createSimpleMarkers(features);
		map.addMarkers(markers);
	}

	public void draw() {
		background(160);
		map.draw();
	}

}

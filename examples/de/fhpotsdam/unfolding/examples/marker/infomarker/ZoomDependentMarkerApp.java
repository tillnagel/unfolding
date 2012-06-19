package de.fhpotsdam.unfolding.examples.marker.infomarker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * TODO Implement with new mechanism.
 * // TO BE DELETED! See labelmarker.* examples for how to do this now.
 * 
 * Shows different set of markers depending on the zoom level.
 */
public class ZoomDependentMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(InfoMarkerApp.class);

	Map map;
	MarkerManager<Marker> labeledMarkerManager;
	MarkerManager<Marker> labeledCountryMarkerManager;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		PFont font = loadFont("Miso-Light-12.vlw");

		map = new Map(this, "map", 10, 10, 780, 580, true, false, new OpenStreetMap.CloudmadeProvider(
				MapDisplayFactory.OSM_API_KEY, 23058));
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Marker> labeledMarkers = GeoRSSLoader.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);
		List<Marker>labeledCountryMarkers = GeoRSSLoader.loadGeoRSSMarkers(this, "bbc-georss-countrytest.xml", font);
		labeledCountryMarkerManager = new MarkerManager<Marker>(labeledCountryMarkers);
		labeledMarkerManager = new MarkerManager<Marker>(labeledMarkers);
		map.addMarkerManager(labeledCountryMarkerManager);
		map.addMarkerManager(labeledMarkerManager);
	}

	float oldZoomLevel = 0;

	public void draw() {
		background(0);

		float zoomLevel = map.getZoomLevel();
		if (oldZoomLevel != zoomLevel) {
			if (zoomLevel > 3) {
				labeledMarkerManager.enableDrawing();
				labeledCountryMarkerManager.disableDrawing();
			} else {
				labeledMarkerManager.disableDrawing();
				labeledCountryMarkerManager.enableDrawing();
			}

			oldZoomLevel = zoomLevel;
		}

		map.draw();
	}

}

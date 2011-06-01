package de.fhpotsdam.unfolding.examples.marker;

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

public class ZoomDependentMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(InfoMarkerApp.class);

	Map map;
	List<Marker> labeledMarkers = new ArrayList<Marker>();
	List<Marker> labeledCountryMarkers = new ArrayList<Marker>();
	MarkerManager markerManager = null;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		PFont font = loadFont("Miso-Light-12.vlw");

		map = new Map(this, "map", 10, 10, 780, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		labeledMarkers = InfoMarkerApp.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);
		labeledCountryMarkers = InfoMarkerApp.loadGeoRSSMarkers(this, "bbc-georss-countrytest.xml",
				font);
		markerManager = new MarkerManager(map, labeledCountryMarkers);
		map.mapDisplay.setMarkerManager(markerManager);
	}

	float oldZoomLevel = 0;

	public void draw() {
		background(0);

		map.draw();

		float zoomLevel = map.getZoomLevel();
		if (oldZoomLevel != zoomLevel) {
			if (zoomLevel > 3) {
				markerManager.setMarkers(labeledMarkers);
			} else {
				markerManager.setMarkers(labeledCountryMarkers);
			}

			oldZoomLevel = zoomLevel;
		}
	}

}

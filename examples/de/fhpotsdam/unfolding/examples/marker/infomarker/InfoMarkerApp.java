package de.fhpotsdam.unfolding.examples.marker.infomarker;

import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.AbstractMarkerManager;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.MapUtils;

//TO BE DELETED! See labelmarker.* examples for how to do this now.
public class InfoMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(ZoomDependentMarkerApp.class);

	UnfoldingMap map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new UnfoldingMap(this, "map", 10, 10, 780, 580);
		MapUtils.createDefaultEventDispatcher(this, map);

		PFont font = loadFont("Miso-Light-12.vlw");
		// Create markers and add them to the MarkerManager
		List<Marker> labeledMarkers = GeoRSSLoader.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);
		AbstractMarkerManager<Marker> markerManager = new MarkerManager<Marker>(labeledMarkers);
		map.addMarkerManager(markerManager);
	}

	public void draw() {
		background(0);

		// Draws map, and all markers connected to this map from the MarkerManager
		map.draw();
	}

	public void mouseMoved() {
		AbstractMarkerManager<? extends Marker> mm = map.mapDisplay.getLastMarkerManager();

		// Deselect all marker
		for (Marker lm : mm.getMarkers()) {
			lm.setSelected(false);
		}

		// Select hit marker
		Marker marker = mm.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}

	}
}

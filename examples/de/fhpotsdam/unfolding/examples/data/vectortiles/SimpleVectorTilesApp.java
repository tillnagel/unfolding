package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays markers of a single vector tile.
 * 
 * Click on the map to load all buildings of vector tile for the area.
 */
public class SimpleVectorTilesApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;
	VectorTilesUtils vectorTilesUtils;

	String featureLayer = "buildings";

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleVectorTilesApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);

		vectorTilesUtils = new VectorTilesUtils(this, map);
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, width / 2, height / 2);
		map.addMarkers(markers);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}

	public void mouseClicked() {
		map.getDefaultMarkerManager().clearMarkers();
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, mouseX, mouseY);
		map.addMarkers(markers);

	}

}

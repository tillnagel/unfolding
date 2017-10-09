package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays markers of a single vector tile.
 * 
 * Click on the map to load all buildings of vector tile for the area.
 */
public class DistanceBasedColoredVectorTilesApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;
	VectorTilesUtils vectorTilesUtils;

	String featureLayer = "buildings";

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { DistanceBasedColoredVectorTilesApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);

		vectorTilesUtils = new VectorTilesUtils(this, map, VectorTilesApp.MAPZEN_API_KEY);
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, width / 2, height / 2);
		map.addMarkers(markers);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}

	public void mouseClicked() {
		map.getDefaultMarkerManager().clearMarkers();
		// List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, mouseX, mouseY);
		List<Marker> markers = vectorTilesUtils.loadMarkersForCurrentMapView(featureLayer);
		map.addMarkers(markers);
	}

	public void mouseMoved() {
		Location mouseLocation = map.getLocation(mouseX, mouseY);
		List<Marker> markers = map.getMarkers();
		for (Marker marker : markers) {

			marker.setStrokeColor(color(221, 221, 221));

			if (marker instanceof AbstractShapeMarker) {
				// Neither polyMarker.getCentroid() nor GeoUtils.getCentroid(m.locations) return correct centroid.
				Location centroid = GeoUtils.getEuclideanCentroid(((AbstractShapeMarker) marker).getLocations());

				// Shade based on distance
				float dist = (float) centroid.getDistance(mouseLocation);
				if (dist < 0.3) {
					float colorValue = map(dist, 0, 0.3f, 0, 238);
					marker.setColor(color(238, colorValue, colorValue, 200));
				} else {
					marker.setColor(color(238, 238, 235));
				}
			}
		}
	}

}

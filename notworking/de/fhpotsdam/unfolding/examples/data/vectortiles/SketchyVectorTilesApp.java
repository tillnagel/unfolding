package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.ArrayList;
import java.util.List;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays markers of a single vector tile.
 * 
 * Click on the map to load all buildings of vector tile for the area.
 */
public class SketchyVectorTilesApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;
	VectorTilesUtils vectorTilesUtils;

	String featureLayer = "buildings";

	List<Feature> features = new ArrayList<Feature>();

	HandyRenderer handy;

	public void settings() {
		size(1920, 1080, JAVA2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SketchyVectorTilesApp.class.getName() });
	}

	public void setup() {
		handy = new HandyRenderer(this);

		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);

		// map.setBackgroundColor(bgColor);

		debugDisplay = new DebugDisplay(this, map);

		vectorTilesUtils = new VectorTilesUtils(this, map);
		features = vectorTilesUtils.loadFeaturesForScreenPos(featureLayer, width / 2, height / 2);

		map.addMarkers(createSketchMarkers(features));
	}

	public List<Marker> createSketchMarkers(List<Feature> features) {
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(SketchyPolygonMarker.class);
		List<Marker> markers = markerFactory.createMarkers(features);
		for (Marker m : markers) {
			if (m instanceof SketchyPolygonMarker) {
				((SketchyPolygonMarker) m).setHandyRenderer(handy);
			} else if (m instanceof MultiMarker) {
				
				MultiMarker mm = (MultiMarker) m;
				for (Marker subMarker : mm.getMarkers()) {
					if (subMarker instanceof SketchyPolygonMarker) {
						((SketchyPolygonMarker) subMarker).setHandyRenderer(handy);
					}
				}
			}
		}
		return markers;
	}

	public void draw() {
		background(240);
		map.draw(); 
		// debugDisplay.draw();
	}

	public void mouseClicked() {
		features = vectorTilesUtils.loadFeaturesForScreenPos(featureLayer, mouseX, mouseY);
		map.addMarkers(createSketchMarkers(features));
	}

	public void keyPressed() {
		if (key == ' ') {
			map.getDefaultMarkerManager().clearMarkers();
		}
	}

}

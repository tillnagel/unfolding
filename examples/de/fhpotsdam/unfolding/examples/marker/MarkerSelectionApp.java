package de.fhpotsdam.unfolding.examples.marker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Loads country markers, and highlights a polygon when the user hovers over it.
 * 
 * This example starts in Southeast Asia to demonstrate hovering multi-marker polygons such as Indonesia, Phillipines,
 * etc.
 */
public class MarkerSelectionApp extends PApplet {

	UnfoldingMap map;
	List<Marker> countryMarkers = new ArrayList<Marker>();
	Location indonesiaLocation = new Location(-6.175, 106.82);

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(3, indonesiaLocation);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null)
			marker.setSelected(true);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MarkerSelectionApp.class.getName() });
	}
}

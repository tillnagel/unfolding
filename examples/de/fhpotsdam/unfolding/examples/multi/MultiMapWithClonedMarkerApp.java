package de.fhpotsdam.unfolding.examples.multi;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/**
 * Clones marker to be used in coordinated views, but with different styles and/or functionality.
 * Manual implementation of clone; works for simple polygons only (not for e.g. France). 
 * 
 * Different to normal case: For displaying same markers in multiple maps, simply add it to both maps.
 */
public class MultiMapWithClonedMarkerApp extends PApplet {

	UnfoldingMap map;
	UnfoldingMap map2;

	public void settings() {
		size(800, 600, P2D);
		smooth();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MultiMapWithClonedMarkerApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, 50, 50, 500, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		map2 = new UnfoldingMap(this, 560, 50, 200, 200);

		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
	}

	public void draw() {
		background(160);
		map.draw();
		map2.draw();
	}

	Marker selectedMarker = null;

	public void mouseClicked() {
		if (selectedMarker != null) {
			selectedMarker.setColor(color(100));
		}
		selectedMarker = map.getFirstHitMarker(mouseX, mouseY);
		selectedMarker.setColor(color(255, 0, 0));

		Marker clonedMarker = clone(selectedMarker);
		if (clonedMarker != null) {
			clonedMarker.setColor(color(0, 255, 0));
			map2.getDefaultMarkerManager().clearMarkers();
			map2.addMarker(clonedMarker);
		}
	}

	public Marker clone(Marker marker) {
		Marker clonedMarker = null;

		if (marker instanceof SimplePolygonMarker) {
			SimplePolygonMarker polyMarker = (SimplePolygonMarker) marker;

			clonedMarker = new SimplePolygonMarker(polyMarker.getLocations());
			clonedMarker.setProperties(polyMarker.getProperties());
		}

		return clonedMarker;
	}

}

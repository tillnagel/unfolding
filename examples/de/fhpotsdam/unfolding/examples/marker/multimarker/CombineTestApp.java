package de.fhpotsdam.unfolding.examples.marker.multimarker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Combines different markers in one MultiMarker. Only that MultiMarker then is displayed.
 * 
 * Note how France is a MultiMarker by itself (France and Corsica).
 */
public class CombineTestApp extends PApplet {

	UnfoldingMap map;
	String[] ids = { "DEU", "FRA", "IRL" };
	List<String> specialIDs = new ArrayList<String>(Arrays.asList(ids));

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { CombineTestApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this);

		// Load all countries
		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);

		// But only combine Germany, France, and Ireland
		MultiMarker multiMarker = new MultiMarker();
		for (Marker marker : countryMarkers) {
			if (specialIDs.contains(marker.getId())) {
				multiMarker.addMarkers(marker);
			}
		}
		map.addMarkers(multiMarker);

		// Zoom in, and center around MultiMarker
		map.zoomToLevel(4);
		map.panTo(multiMarker.getLocation());
	}

	public void draw() {
		map.draw();
	}

}

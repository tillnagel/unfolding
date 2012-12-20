package de.fhpotsdam.unfolding.examples.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class CombineTestApp extends PApplet {

	UnfoldingMap map;
	String[] ids = { "DEU", "FRA", "BEL" };
	List<String> specialIDs = new ArrayList<String>(Arrays.asList(ids));

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);

		List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);

		MultiMarker multiMarker = new MultiMarker();

		for (Marker marker : countryMarkers) {
			if (specialIDs.contains(marker.getId())) {
				multiMarker.addMarkers(marker);
			}
		}
		map.addMarkers(multiMarker);
	}

	public void draw() {
		map.draw();
	}

}

package de.fhpotsdam.unfolding.utils;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;

public class GeoUtilsTestApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600);

		List<Feature> features = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Location> locations = GeoUtils.getLocations(features);
		println("Found " + locations.size() + " locations");
	}

	public void draw() {

	}
}

package de.fhpotsdam.unfolding.examples.data.countrydata;

import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows data bubbles for each country. Loads country shapes, and calculates the centroid to create point marker.
 * 
 * This example loads polygon features (countries) and displays a single data marker (population) in their center.
 * 
 * Uses the data CSV for all countries from http://opengeocode.org/download/cow.php
 */
public class CountryBubbleMapApp extends PApplet {

	UnfoldingMap map;

	HashMap<String, DataEntry> dataEntriesMap;
	List<Marker> countryMarkers;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(2);
		map.setBackgroundColor(240);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load country polygons and adds them as markers
		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		// countryMarkers = MapUtils.createSimpleMarkers(countries);

		// Load population data
		dataEntriesMap = loadPopulationFromCSV("data/countries-data.csv");

		for (Feature country : countries) {
			// Calculates center to show single point marker for polygon feature
			Location location = GeoUtils.getCentroid(country, true);

			if (location != null) {
				SimplePointMarker marker = new SimplePointMarker(location);

				String countryId = country.getId();
				DataEntry dataEntry = dataEntriesMap.get(countryId);
				if (dataEntry != null) {
					// Map to correct size (linearly to area)
					float radius = PApplet.sqrt(dataEntry.value / PApplet.PI);
					// Constrain radius to 100px (20000 is for China's population of 1.3billion)
					float s = map(radius, 0, 20000, 0, 100);
					// Set size of marker
					marker.setRadius(s);
					map.addMarkers(marker);
				}
			}
		}
	}

	public void draw() {
		background(240);

		// Draw map tiles and country markers
		map.draw();
	}

	public HashMap<String, DataEntry> loadPopulationFromCSV(String fileName) {
		HashMap<String, DataEntry> dataEntriesMap = new HashMap<String, DataEntry>();

		String[] rows = loadStrings(fileName);
		int i = 0;
		for (String row : rows) {
			i++;
			if (i < 30) {
				continue;
			}
			// Reads country name and population density value from CSV row
			String[] columns = row.split(";");
			if (columns.length >= 52) {
				DataEntry dataEntry = new DataEntry();
				dataEntry.countryName = columns[4]; // country name
				dataEntry.id = columns[1].trim(); // 3 letter ISO code
				dataEntry.value = Float.parseFloat(columns[52]); // population
				dataEntriesMap.put(dataEntry.id, dataEntry);
			}
		}

		return dataEntriesMap;
	}

	public class DataEntry {
		String countryName;
		String id;
		Integer year;
		Float value;
	}

}

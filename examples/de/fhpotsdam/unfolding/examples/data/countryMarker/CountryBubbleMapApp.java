package de.fhpotsdam.unfolding.examples.data.countryMarker;

import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
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
 * ATTENTION: The used data set does not make sense, at the moment (population density!)
 */
public class CountryBubbleMapApp extends PApplet {

	UnfoldingMap map;

	HashMap<String, DataEntry> dataEntriesMap;
	List<Marker> countryMarkers;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(2);
		map.setBackgroundColor(240);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load country polygons and adds them as markers
		List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		// countryMarkers = MapUtils.createSimpleMarkers(countries);

		// Load population data
		dataEntriesMap = loadPopulationDensityFromCSV("countries-population-density.csv");

		for (Feature country : countries) {
			Location location = GeoUtils.getCentroid(country, true);
			if (location != null) {
				SimplePointMarker marker = new SimplePointMarker(location);

				String countryId = country.getId();
				DataEntry dataEntry = dataEntriesMap.get(countryId);
				if (dataEntry != null) {
					println("Country " + countryId + " = " + dataEntry.countryName);
					float s = map(dataEntry.value, 0, 1000, 0, 100);
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

	public HashMap<String, DataEntry> loadPopulationDensityFromCSV(String fileName) {
		HashMap<String, DataEntry> dataEntriesMap = new HashMap<String, DataEntry>();

		String[] rows = loadStrings(fileName);
		for (String row : rows) {
			// Reads country name and population density value from CSV row
			String[] columns = row.split(";");
			if (columns.length >= 3) {
				DataEntry dataEntry = new DataEntry();
				dataEntry.countryName = columns[0];
				dataEntry.id = columns[1];
				dataEntry.value = Float.parseFloat(columns[2]);
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

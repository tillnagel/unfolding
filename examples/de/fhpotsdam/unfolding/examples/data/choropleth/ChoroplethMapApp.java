package de.fhpotsdam.unfolding.examples.data.choropleth;

import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Visualizes population density of the world as a choropleth map. Countries are shaded in proportion to the population
 * density.
 * 
 * It loads the country shapes from a GeoJSON file via a data reader, and loads the population density values from
 * another CSV file (provided by the World Bank). For the encoding a linear mapping to the transparency value is done.
 */
public class ChoroplethMapApp extends PApplet {

	Map map;

	HashMap<String, DataEntry> dataEntriesMap;
	List<Marker> countryMarkers;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new Map(this, 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);

		dataEntriesMap = loadPopulationDensityFromCSV("countries-population-density.csv");
		println("Loaded " + dataEntriesMap.size() + " data entries");
		shadeCountries();
	}

	public void draw() {
		background(160);
		map.draw();
	}

	public void shadeCountries() {
		for (Marker marker : countryMarkers) {
			// TODO Use MarkerManager<E extends Marker> to avoid casting
			SimplePolygonMarker spMarker = (SimplePolygonMarker) marker;
			String countryName = (String) spMarker.getProperties().get("name");
			DataEntry dataEntry = dataEntriesMap.get(countryName);
			if (dataEntry != null && dataEntry.value != null) {
				// Encode value as brightness (values range: 0-1000)
				float transparency = map(dataEntry.value, 0, 700, 10, 255);
				spMarker.setColor(color(255, 0, 0, transparency));
			} else {
				// No value available
				spMarker.setColor(color(100, 120));
			}
		}
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
				dataEntry.value = Float.parseFloat(columns[2]);
				dataEntriesMap.put(dataEntry.countryName, dataEntry);
			}
		}

		return dataEntriesMap;
	}

	class DataEntry {
		String countryName;
		Integer year;
		Float value;
	}

	// ------------------------------------------

	/**
	 * Test loading method to load from original XML file from WorldBank.
	 */
	public HashMap<String, DataEntry> loadPopulationDensityFromXML(String fileName) {
		HashMap<String, DataEntry> dataEntriesMap = new HashMap<String, DataEntry>();

		// Get all records
		XMLElement rss = new XMLElement(this, fileName);
		XMLElement[] records = rss.getChildren("data/record");
		for (int i = 0; i < records.length; i++) {
			DataEntry dataEntry = new DataEntry();

			XMLElement[] fields = records[i].getChildren("field");
			for (int j = 0; j < fields.length; j++) {
				XMLElement field = fields[j];
				String fieldName = field.getString("name");

				if (fieldName.equals("Country or Area")) {
					dataEntry.countryName = field.getContent();
				} else if (fieldName.equals("Year")) {
					dataEntry.year = Integer.parseInt(field.getContent());

				} else if (fieldName.equals("Value")) {
					String valueStr = field.getContent();
					if (valueStr != null) {
						dataEntry.value = Float.parseFloat(valueStr);
					}
				}
			}

			if (dataEntry.year == 2010) {
				dataEntriesMap.put(dataEntry.countryName, dataEntry);
			}
		}
		return dataEntriesMap;
	}

}

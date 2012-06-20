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
 * another XML file (provided by the World Bank). For the encoding a linear mapping to 
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

		dataEntriesMap = loadPopulationDensityFromXML("countries-population-density.xml");
		println("Loaded " + dataEntriesMap.size() + " data entries");
		shadeCountries();
	}

	public void draw() {
		background(160);
		map.draw();
	}

	public void keyPressed() {
		//shadeCountries();
	}

	public void shadeCountries() {
		float maxValue = 0;
		float minValue = 1142;
		for (Marker marker : countryMarkers) {
			// TODO Use MarkerManager<E extends Marker> to avoid casting
			SimplePolygonMarker spMarker = (SimplePolygonMarker) marker;
			String countryName = (String) spMarker.getProps().get("name");
			DataEntry dataEntry = dataEntriesMap.get(countryName);
			if (dataEntry != null && dataEntry.value != null) {
				float saturation = map(dataEntry.value, 0, 700, 10, 255);
				spMarker.setColor(color(255, 0, 0, saturation));
				
				minValue = min(minValue, dataEntry.value);
				maxValue = max(maxValue, dataEntry.value);
				
			} else {
				// No value available
				spMarker.setColor(color(100, 120));
			}
		}

		println("minValue:" + minValue + ", maxValue:" + maxValue);
	}

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
					dataEntry.country = field.getContent();
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
				dataEntriesMap.put(dataEntry.country, dataEntry);
			}
		}
		return dataEntriesMap;
	}

	class DataEntry {
		String country;
		Integer year;
		Float value;
	}

}

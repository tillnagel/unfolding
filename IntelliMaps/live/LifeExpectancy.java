package live;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.providers.Google.*;

import java.util.List;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;

import java.util.HashMap;
import java.util.Map;

import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Visualizes life expectancy in different countries.
 * 
 * It loads the country shapes from a GeoJSON file via a data reader, and loads
 * the population density values from
 * another CSV file (provided by the World Bank). The data value is encoded to
 * transparency via a simplistic linear
 * mapping.
 */
public class LifeExpectancy extends PApplet {

	/*
	 * Visualizes life expectancy in different countries.
	 * 
	 * It loads the country shapes from a GeoJSON file via a data reader, and loads
	 * the population density values from
	 * another CSV file (provided by the World Bank). The data value is encoded to
	 * transparency via a simplistic linear
	 * mapping.
	 */
	
	private static final long serialVersionUID = 1674612410756573228L;
	UnfoldingMap map;
	Map<String, Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 0, 0, 800, 600, new Microsoft.RoadProvider());
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load lifeExpectancy data
		lifeExpByCountry = loadLifeExpectancyFromCSV(
				"C:\\Users\\achar\\OneDrive\\Documents\\GitHub\\IntelliMaps\\data\\LifeExpectancyWorldBank.csv");
		println("Loaded " + lifeExpByCountry.size() + " data entries");

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);

		// Country markers are shaded according to life expectancy (only once)
		shadeCountries();
	}

	public void draw() {
		// Draw map tiles and country markers
		map.draw();
	}

	// Helper method to color each country based on life expectancy
	// Red-orange indicates low (near 40)
	// Blue indicates high (near 100)
	private void shadeCountries() {
		for (Marker marker : countryMarkers) {
			// Find data for country of the current marker
			String countryId = marker.getId();
			if (lifeExpByCountry.containsKey(countryId)) {
				float lifeExp = lifeExpByCountry.get(countryId);
				// Encode value as brightness (values range: 53-85)
				int colorLevel = (int) map(lifeExp, 50, 90, 10, 255);
				marker.setColor(color(255 - colorLevel, 100, colorLevel));
			} else {
				marker.setColor(color(150, 150, 150));
			}
		}
	}

	// Helper method to load life expectancy data from file
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) {
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();

		String[] rows = loadStrings(fileName);
		for (String row : rows) {

			// Reads country name and population density value from CSV row
			String[] columns = row.split(",");
			if (columns.length == 6 && !columns[5].equals("..")) {
				lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
			}
		}

		return lifeExpMap;
	}

}

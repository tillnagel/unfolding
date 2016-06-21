import java.util.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.utils.*;


UnfoldingMap map;

HashMap<String, DataEntry> dataEntriesMap;
List<Marker> countryMarkers;


void setup() {
  size(900, 600, P2D);

  map = new UnfoldingMap(this);
  map.zoomToLevel(2);
  map.setBackgroundColor(240);
  MapUtils.createDefaultEventDispatcher(this, map);

  loadDataAndCreateMarkers();
}

void draw() {
  background(240);

  // Draw map tiles and country markers
  map.draw();
}

void loadDataAndCreateMarkers() {
  
  // Load country polygons
  // Countries are used to get center points, but not displayed as polygon markers on the map.
  List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");

  // Load population data
  dataEntriesMap = loadPopulationFromCSV("data/countries-data.csv");

  for (Feature country : countries) {
    // Calculates center to show single point marker for polygon feature
    Location location = GeoUtils.getCentroid(country, true);

    if (location != null) {
      SimplePointMarker marker = new SimplePointMarker(location);

      String countryId = country.getId();
      DataEntry dataEntry = dataEntriesMap.get(countryId);
      if (dataEntry != null && dataEntry.value > 0) {
        // Map population (up to China's 1.3 billion) to area of circle with a max radius of 100px 
        float normPop = map(dataEntry.value, 0, 1300000000, 0, pow(100, 2) * PI);
        float radius = sqrt(normPop / PI);

        // Set size of marker
        marker.setRadius(radius);
        map.addMarkers(marker);
      }
    }
  }
}

// Loads population
HashMap<String, DataEntry> loadPopulationFromCSV(String fileName) {
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
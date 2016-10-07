package de.fhpotsdam.unfolding.examples.data.choropleth;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Visualizes population density of the world as a choropleth map. Countries are shaded in proportion to the population
 * density.
 * <p>
 * It loads the country shapes from a GeoJSON file via a data reader, and loads the population density values from
 * another CSV file (provided by the World Bank). The data value is encoded to transparency via a simplistic linear
 * mapping.
 */
public class ChoroplethMapApp extends PApplet {

    private UnfoldingMap map;
    private HashMap<String, DataEntry> dataEntriesMap;
    private List<Marker> countryMarkers;
    private static final Logger LOGGER = Logger.getLogger(ChoroplethMapApp.class);

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{ChoroplethMapApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, 50, 50, 700, 500);
        map.zoomToLevel(2);
        map.setBackgroundColor(240);
        MapUtils.createDefaultEventDispatcher(this, map);

        // Load country polygons and adds them as markers
        final List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
        countryMarkers = MapUtils.createSimpleMarkers(countries);
        map.addMarkers(countryMarkers);

        // Load population data
        dataEntriesMap = loadPopulationDensityFromCSV("data/countries-population-density.csv");
        LOGGER.info("Loaded " + dataEntriesMap.size() + " data entries");

        // Country markers are shaded according to its population density (only once)
        shadeCountries();
    }

    @Override
    public void draw() {
        background(240);

        // Draw map tiles and country markers
        map.draw();
    }

    private void shadeCountries() {
        for (Marker marker : countryMarkers) {
            // Find data for country of the current marker
            final String countryId = marker.getId();
            final DataEntry dataEntry = dataEntriesMap.get(countryId);

            if (dataEntry != null && dataEntry.value != null) {
                // Encode value as brightness (values range: 0-1000)
                final float transparency = map(dataEntry.value, 0, 700, 10, 255);
                marker.setColor(color(255, 0, 0, transparency));
            } else {
                // No value available
                marker.setColor(color(100, 120));
            }
        }
    }

    private HashMap<String, DataEntry> loadPopulationDensityFromCSV(final String fileName) {
        final HashMap<String, DataEntry> dataEntriesMap = new HashMap<String, DataEntry>();
        final String[] rows = loadStrings(fileName);
        for (String row : rows) {
            // Reads country name and population density value from CSV row
            String[] columns = row.split(";");
            if (columns.length >= 3) {
                final DataEntry dataEntry = new DataEntry();
                dataEntry.countryName = columns[0];
                dataEntry.id = columns[1];
                dataEntry.value = Float.parseFloat(columns[2]);
                dataEntriesMap.put(dataEntry.id, dataEntry);
            }
        }
        return dataEntriesMap;
    }

}

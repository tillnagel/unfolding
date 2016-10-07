package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Loads vector tiles, and displays museums in a different color.
 * <p>
 * Click on the map to load its vector tiles.
 */
public class ColoredSelectedVectorTilesApp extends PApplet {

    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private VectorTilesUtils vectorTilesUtils;
    private static final String FILTERED_TYPE = "museum";

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{ColoredSelectedVectorTilesApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, "myMap");
        map.zoomAndPanTo(16, new Location(52.501, 13.395));
        MapUtils.createDefaultEventDispatcher(this, map);

        debugDisplay = new DebugDisplay(this, map);

        vectorTilesUtils = new VectorTilesUtils(this, map);

        loadAndAddColoredMarkers(width / 2, height / 2, FILTERED_TYPE);
    }

    @Override
    public void draw() {
        map.draw();
        debugDisplay.draw();
    }

    @Override
    public void mouseClicked() {
        loadAndAddColoredMarkers(mouseX, mouseY, FILTERED_TYPE);
    }

    private void loadAndAddColoredMarkers(final int x, final int y, final String filteredType) {
        final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos("buildings", x, y);
        for (final Marker marker : markers) {
            final String kind = marker.getStringProperty("kind");
            if (filteredType.equals(kind)) {
                marker.setColor(color(0, 255, 0, 200));
            }
        }
        map.addMarkers(markers);
    }

}

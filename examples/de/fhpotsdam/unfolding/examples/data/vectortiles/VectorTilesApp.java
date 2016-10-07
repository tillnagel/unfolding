package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

import static de.fhpotsdam.unfolding.examples.data.vectortiles.SelectBuildingsApp.FEATURE_LAYER;

/**
 * Interactive vector tiles example to dynamically load and display buildings from OpenStreetMap.
 * <p>
 * Click on the map to load its vector tiles. Press SPACE to clear markers. Press 'A' to load all vector tiles for the
 * current map view.
 * <p>
 * Handles overlapping features, i.e. features returned in multiple vector tiles are shown only once if the ID is the
 * same. This mechanism does not take into account the zoom factor, i.e. the same feature is not loaded anew for another
 * zoom level.
 */
public class VectorTilesApp extends PApplet {

    private static final Logger LOGGER = Logger.getLogger(VectorTilesApp.class);
    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private VectorTilesUtils vectorTilesUtils;
    private boolean loadUniqueMarkers = true;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{VectorTilesApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, "myMap");
        map.zoomAndPanTo(16, new Location(52.501, 13.395));
        MapUtils.createDefaultEventDispatcher(this, map);

        debugDisplay = new DebugDisplay(this, map);

        vectorTilesUtils = new VectorTilesUtils(this, map);

        final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, width / 2, height / 2);
        map.addMarkers(markers);
    }

    @Override
    public void draw() {
        map.draw();
        debugDisplay.draw();
    }

    @Override
    public void mouseClicked() {
        final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, mouseX, mouseY);
        addMarkers(markers, loadUniqueMarkers);
    }

    @Override
    public void keyPressed() {
        if (key == 'u') {
            loadUniqueMarkers = !loadUniqueMarkers;
        }
        if (key == ' ') {
            map.getDefaultMarkerManager().clearMarkers();
        }
        if (key == 'a') {
            List<Marker> markers = vectorTilesUtils.loadMarkersForCurrentMapView(FEATURE_LAYER);
            addMarkers(markers, loadUniqueMarkers);
        }
    }

    /**
     * @param unique Indicates whether to check for same markers only loaded. If true only new markers are returned, if
     *               false all markers containing possible duplicates.
     */
    public void addMarkers(List<Marker> markers, boolean unique) {
        if (unique) {
            // Add only new markers
            map.addUniqueMarkers(markers);
        } else {
            // Add all markers
            map.addMarkers(markers);
        }
    }

}

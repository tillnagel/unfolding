package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

import static de.fhpotsdam.unfolding.examples.data.vectortiles.SelectBuildingsApp.FEATURE_LAYER;

/**
 * Displays markers of a single vector tile.
 * <p>
 * Click on the map to load all buildings of vector tile for the area.
 */
public class SimpleVectorTilesApp extends PApplet {

    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private VectorTilesUtils vectorTilesUtils;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{SimpleVectorTilesApp.class.getName()});
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
        map.getDefaultMarkerManager().clearMarkers();
        final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, mouseX, mouseY);
        map.addMarkers(markers);

    }

}

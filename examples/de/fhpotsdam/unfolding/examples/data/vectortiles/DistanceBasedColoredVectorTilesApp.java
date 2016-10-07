package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

import static de.fhpotsdam.unfolding.examples.data.vectortiles.SelectBuildingsApp.FEATURE_LAYER;

/**
 * Displays markers of a single vector tile.
 * <p>
 * Click on the map to load all buildings of vector tile for the area.
 */
public class DistanceBasedColoredVectorTilesApp extends PApplet {

    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private VectorTilesUtils vectorTilesUtils;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{DistanceBasedColoredVectorTilesApp.class.getName()});
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
        // List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, mouseX, mouseY);
        final List<Marker> markers = vectorTilesUtils.loadMarkersForCurrentMapView(FEATURE_LAYER);
        map.addMarkers(markers);
    }

    @Override
    public void mouseMoved() {
        final Location mouseLocation = map.getLocation(mouseX, mouseY);
        final List<Marker> markers = map.getMarkers();
        for (final Marker marker : markers) {

            marker.setStrokeColor(color(221, 221, 221));

            if (marker instanceof AbstractShapeMarker) {
                // Neither polyMarker.getCentroid() nor GeoUtils.getCentroid(m.locations) return correct centroid.
                final Location centroid = GeoUtils.getEuclideanCentroid(((AbstractShapeMarker) marker).getLocations());

                // Shade based on distance
                final float dist = (float) centroid.getDistance(mouseLocation);
                if (dist < 0.3) {
                    final float colorValue = map(dist, 0, 0.3f, 0, 238);
                    marker.setColor(color(238, colorValue, colorValue, 200));
                } else {
                    marker.setColor(color(238, 238, 235));
                }
            }
        }
    }

}

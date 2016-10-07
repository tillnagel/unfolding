package de.fhpotsdam.unfolding.examples.marker.cluster;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

import static de.fhpotsdam.unfolding.examples.data.styled.GeoRSSStyledMarkerApp.EARTHQUAKES_URL;

/**
 * Simple interactive marker grid example.
 * <p>
 * Counts all markers within a grid region, and colors the rectangle in proportion. Grid size is constant, thus number
 * of markers depend on map area and zoom.
 */
public class MarkerGridApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        smooth();
        map = new UnfoldingMap(this);
        map.zoomToLevel(2);
        MapUtils.createDefaultEventDispatcher(this, map);

        final List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, EARTHQUAKES_URL);
        final List<Marker> markers = MapUtils.createSimpleMarkers(features);
        map.addMarkers(markers);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();
        drawGrid();
    }

    private void drawGrid() {
        int gridWidth = 100;
        int gridHeight = 100;
        for (int x = 0; x < width; x += gridWidth) {
            for (int y = 0; y < height; y += gridHeight) {
                int insideMarkerNumber = 0;

                // Count markers inside the current grid rectangle
                final MarkerManager<Marker> markerManager = map.getDefaultMarkerManager();
                for (Marker m : markerManager.getMarkers()) {
                    final ScreenPosition pos = map.getScreenPosition(m.getLocation());
                    if (pos.x > x && pos.x < x + gridWidth && pos.y > y && pos.y < y + gridHeight) {
                        insideMarkerNumber++;
                    }
                }

                // Map number to color
                float alpha = map(insideMarkerNumber, 0, 10, 0, 255);

                // Draw current grid rectangle
                fill(255, 0, 0, alpha);
                rect(x, y, gridWidth, gridHeight);

            }
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{MarkerGridApp.class.getName()});
    }

}

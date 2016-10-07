package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays countries of the world as simple polygons.
 * <p>
 * Reads from a GeoJSON file, and uses default marker creation. Features are polygons.
 * <p>
 * Press SPACE to toggle visibility of the polygons.
 */
public class GeoJSONMarkerApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
        smooth();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{GeoJSONMarkerApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, 50, 50, 700, 500);
        map.zoomToLevel(2);
        MapUtils.createDefaultEventDispatcher(this, map);

        final List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
        final List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
        map.addMarkers(countryMarkers);
    }

    @Override
    public void draw() {
        background(160);
        map.draw();
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            map.getDefaultMarkerManager().toggleDrawing();
        }
    }

}

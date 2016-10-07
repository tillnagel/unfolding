package de.fhpotsdam.unfolding.examples.marker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Loads country markers, and highlights a polygon when the user hovers over it.
 * <p>
 * This example starts in Southeast Asia to demonstrate hovering multi-marker polygons such as Indonesia, Phillipines,
 * etc.
 */
public class MarkerSelectionApp extends PApplet {

    private UnfoldingMap map;
    private Location indonesiaLocation = new Location(-6.175, 106.82);

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(indonesiaLocation, 3);
        MapUtils.createDefaultEventDispatcher(this, map);

        final List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
        final List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
        map.addMarkers(countryMarkers);
    }

    @Override
    public void draw() {
        background(240);
        map.draw();
    }

    @Override
    public void mouseMoved() {
        for (Marker marker : map.getMarkers()) {
            marker.setSelected(false);
        }
        final Marker marker = map.getFirstHitMarker(mouseX, mouseY);
        if (marker != null)
            marker.setSelected(true);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{MarkerSelectionApp.class.getName()});
    }
}

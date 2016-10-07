package de.fhpotsdam.unfolding.examples.marker.advanced;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Advanced example to demonstrate custom nearest marker test.
 * <p>
 * getNearestMarker(x,y) returns the closest marker to the given screen position. Using
 * DistancePerLocationPolygonMarker, i.e. instead of the default implementation of using the centroid of the polygon,
 * this tests all vertices of the polygon.
 */
@SuppressWarnings("serial")
public class GetNearestMultiMarkerApp extends PApplet {

    private UnfoldingMap map;
    private List<Marker> countryMarkers = new ArrayList<Marker>();

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomToLevel(2);
        map.panTo(new Location(58.631217f, -101.601562f));
        MapUtils.createDefaultEventDispatcher(this, map);

        initPolygons();
    }

    @Override
    public void draw() {
        background(240);
        map.draw();
    }

    @Override
    public void mouseMoved() {
        final Marker marker = map.getDefaultMarkerManager().getNearestMarker(mouseX, mouseY);
        if (marker != null) {
            for (Marker countryMarker : countryMarkers) {
                countryMarker.setSelected(countryMarker == marker);
            }
        }
    }

    private void initPolygons() {
        final List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
        final List<Feature> selectedCountries = new ArrayList<Feature>();
        for (Feature feature : countries) {
            if (feature.getId().equalsIgnoreCase("CAN") || feature.getId().equalsIgnoreCase("USA")) {
                selectedCountries.add(feature);
            }
        }
        final MarkerFactory markerFactory = new MarkerFactory();
        markerFactory.setPolygonClass(DistancePerLocationPolygonMarker.class);
        countryMarkers = markerFactory.createMarkers(selectedCountries);
        map.addMarkers(countryMarkers);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{GetNearestMultiMarkerApp .class.getName()});
    }

}

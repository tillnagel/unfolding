package de.fhpotsdam.unfolding.data.custommulti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MultiMarkerTestApp extends PApplet {

    UnfoldingMap map;
    String[] ids = {"NLD", "FRA", "IRL", "AUT", "GBR"};
    List<String> specialIDs = new ArrayList<>(Arrays.asList(ids));

    @Override
    public void settings() {
        size(800, 600, P2D);
    }
    
    @Override
    public void setup() {
        List<Feature> countryFeatures = GeoJSONReader.loadData(this, "data/countries.geo.json");
        // List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countryFeatures);

        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);
        map.zoomAndPanTo(5, new Location(48, 5));

        MarkerFactory markerFactory = new MarkerFactory();
        markerFactory.setMultiClass(ConvexHullMultiMarker.class);
        List<Marker> countryMarkers = markerFactory.createMarkers(countryFeatures);
        map.addMarkers(filterMarkers(countryMarkers));
    }

    @Override
    public void draw() {
        map.draw();
    }

    @Override
    public void mouseMoved() {
        for (Marker marker : map.getMarkers()) {
            marker.setSelected(false);
        }
        Marker marker = map.getFirstHitMarker(mouseX, mouseY);
        if (marker != null) {
            marker.setSelected(true);
        }
    }

    private List<Marker> filterMarkers(List<Marker> markers) {
        List<Marker> filteredMarkers = new ArrayList<>();
        for (Marker marker : markers) {
            if (specialIDs.contains(marker.getId())) {
                filteredMarkers.add(marker);
            }
        }
        return filteredMarkers;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{MultiMarkerTestApp.class.getName()});
    }
}

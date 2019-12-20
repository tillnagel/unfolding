package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays earthquake markers from an RSS feed for the last 7 days.
 *
 * Reads from GeoRSS file, and uses default marker creation.
 *
 * Features are points (positions of earthquakes).
 */
public class GeoRSSMarkerApp extends PApplet {

    String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom";

    UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
        smooth();
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomToLevel(2);
        MapUtils.createDefaultEventDispatcher(this, map);

        List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
        List<Marker> markers = MapUtils.createSimpleMarkers(features);
        map.addMarkers(markers);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{GeoRSSMarkerApp.class.getName()});
    }
}

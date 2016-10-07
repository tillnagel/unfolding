package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

import static de.fhpotsdam.unfolding.examples.data.styled.GeoRSSStyledMarkerApp.EARTHQUAKES_URL;

/**
 * Displays earthquake markers from an RSS feed for the last 7 days.
 * <p>
 * Reads from GeoRSS file, and uses default marker creation.
 * <p>
 * Features are points (positions of earthquakes).
 */
public class GeoRSSMarkerApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
        smooth();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{GeoRSSMarkerApp.class.getName()});
    }

    @Override
    public void setup() {
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
    }

}

package de.fhpotsdam.unfolding.examples.data;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GPXReader;
import de.fhpotsdam.unfolding.examples.data.customreader.GPXSpeedTrackApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays a track loaded from a GPX file containing a bike tour in Berlin.
 * <p>
 * See {@link GPXSpeedTrackApp} for custom GPX parsing and marker display.
 */
public class GPXTrackApp extends PApplet {

    private Location startLocation = new Location(52.49f, 13.44f);
    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);
        map.zoomAndPanTo(13, startLocation);

        final List<Feature> features = GPXReader.loadData(this, "data/bike-tour.gpx");
        final List<Marker> markers = MapUtils.createSimpleMarkers(features);
        map.addMarkers(markers);
    }

    @Override
    public void draw() {
        map.draw();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{GPXTrackApp.class.getName()});
    }
}
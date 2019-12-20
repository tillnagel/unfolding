package de.fhpotsdam.generics;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class MarkerTestApp extends PApplet {

    ClassHavingMarkerManager classHavingMarkerManager;

    MarkerManager<SimplePointMarker> markerManager;

    @Override
    public void setup() {
        classHavingMarkerManager = new ClassHavingMarkerManager();
        classHavingMarkerManager.markerManager = new MarkerManager<>();

        markerManager = new MarkerManager<>();

        SimplePointMarker marker = new SimplePointMarker();
        markerManager.addMarker(marker);
    }

    @Override
    public void draw() {
        for (SimplePointMarker marker : markerManager.getMarkers()) {
            marker.getId();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{MarkerTestApp.class.getName()});
    }
}

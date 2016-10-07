package de.fhpotsdam.unfolding.examples.geo;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Automatically zoom and pans to the selected country marker. Click on any country to see it in action.
 * <p>
 * Uses {@link UnfoldingMap#zoomAndPanToFit(Marker)}.
 */
public class ZoomToFitApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);
        map.setTweening(true);

        final List<Feature> countryFeatures = GeoJSONReader.loadData(this, "countries.geo.json");
        final List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countryFeatures);
        map.addMarkers(countryMarkers);
    }

    @Override
    public void draw() {
        background(240);
        map.draw();
    }

    @Override
    public void mouseClicked() {
        final Marker marker = map.getFirstHitMarker(mouseX, mouseY);
        if (marker != null) {
            map.zoomAndPanToFit(marker);
        } else {
            map.zoomAndPanTo(2, new Location(0, 0));
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{ZoomToFitApp.class.getName()});
    }

}

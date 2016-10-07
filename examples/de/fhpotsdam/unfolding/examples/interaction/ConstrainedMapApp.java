package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This map is constrained in both pan and zoom interactions. Users are allowed to only pan within a specified radial
 * area, and to zoom within specific zoom levels.
 */
public class ConstrainedMapApp extends PApplet {

    private static final Location CENTER_LOCATION = new Location(1.359f, 103.816f);
    private static final float MAX_PANNING_DISTANCE = 30;
    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(12, new Location(CENTER_LOCATION));
        map.setPanningRestriction(CENTER_LOCATION, MAX_PANNING_DISTANCE);
        map.setZoomRange(12, 15);

        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{ConstrainedMapApp.class.getName()});
    }

}

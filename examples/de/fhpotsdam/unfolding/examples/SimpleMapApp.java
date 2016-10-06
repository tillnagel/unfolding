package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 * <p>
 * Works correctly under Processing 3.1.2 or later version
 */
public class SimpleMapApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        background(240);
        map.draw();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{SimpleMapApp.class.getName()});
    }
}

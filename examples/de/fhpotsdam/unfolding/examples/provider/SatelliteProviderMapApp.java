package de.fhpotsdam.unfolding.examples.provider;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Show how to use the Microsoft Aerial Tile Provider.
 */
public class SatelliteProviderMapApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, new Microsoft.AerialProvider());
        map.zoomToLevel(3);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SatelliteProviderMapApp.class.getName()});
    }

}
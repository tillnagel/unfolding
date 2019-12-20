package de.fhpotsdam.unfolding;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
public class SimpleMapP2DApp extends PApplet {

    UnfoldingMap map;
    
    @Override
    public void settings() {
        size(1024, 768, P2D);
    }
    @Override
    public void setup() {
        println(g);

        map = new UnfoldingMap(this);
        map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        map.draw();
    }
    
    public static void main(String args[]) {
        PApplet.main(new String[]{SimpleMapP2DApp.class.getName()});
    }
}

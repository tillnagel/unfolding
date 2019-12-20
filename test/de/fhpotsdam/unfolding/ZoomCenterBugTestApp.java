package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class ZoomCenterBugTestApp extends PApplet {

    UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);
        map.zoomAndPanTo(10, new Location(52.52, 13.41));
    }

    @Override
    public void draw() {
        map.draw();
    }

    @Override
    public void keyPressed() {
        map.mapDisplay.setInnerTransformationCenter(new PVector(width / 2, height / 2));
        if (key == 'z') {
            map.zoomToLevel(12);
        }
        if (key == 't') {
            map.zoomIn();
        }

    }

    public static void main(String args[]) {
        PApplet.main(new String[]{ZoomCenterBugTestApp.class.getName()});
    }
}

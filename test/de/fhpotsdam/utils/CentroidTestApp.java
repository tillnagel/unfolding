package de.fhpotsdam.utils;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class CentroidTestApp extends PApplet {

    UnfoldingMap map;
    List<Location> locations = new ArrayList<>();

    @Override
    public void settings() {
        size(800, 600);
        smooth();
    }
    
    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);

        // centroid of all locations
        // centroid of centroids
    }

    @Override
    public void draw() {
        background(0);
        map.draw();

        fill(0);
        for (Location location : locations) {
            ScreenPosition pos = map.getScreenPosition(location);
            ellipse(pos.x, pos.y, 10, 10);
        }

        Location centroid = GeoUtils.getCentroid(locations);
        ScreenPosition centroidPos = map.getScreenPosition(centroid);
        fill(255, 0, 0);
        ellipse(centroidPos.x, centroidPos.y, 10, 10);

    }

    @Override
    public void mouseClicked() {
        Location location = map.getLocation(mouseX, mouseY);
        locations.add(location);
    }

    @Override
    public void keyPressed() {
        println(locations);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{CentroidTestApp.class.getName()});
    }
}

package de.fhpotsdam.unfolding.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays a million markers on the map.
 *
 * (c) 2012 Till Nagel, unfoldingmaps.org
 *
 * Used for various performance tests.
 * <ul>
 * <li>pure drawing (fps for 10k, 100k, 1000k.)</li>
 * <li>pure drawing markers (ditto)</li>
 * <li>cut-off on map border</li>
 * <li>filtering only on map change</li>
 * <li>calculating ScreenPosition only on map change</li>
 * <li>Different visual representations (e.g. rect vs ellipse</li>
 * <li>Use GLModel (see ltavis)</li>
 * <li>...</li>
 * </ul>
 *
 * Outcomes - rect is faster than ellipse (20k. rect: 24fps, ellipse: 7fps)
 *
 */
public class MillionDotsMapApp4Timer extends PApplet {

    UnfoldingMap map;
    List<Dot> dots = new ArrayList<>();
    final List<PVector> visibleDotVertices = new ArrayList<>();

    Location tlLoc;
    Location brLoc;

    @Override
    public void settings() {
        size(800, 600, OPENGL);
        smooth();
    }
    
    @Override
    public void setup() {
        dots = createRandomDots(10000);

        map = new UnfoldingMap(this);
        map.zoomToLevel(3);
        MapUtils.createDefaultEventDispatcher(this, map);

        mapChanged(null);
    }

    @Override
    public void draw() {
        map.draw();

        callTimer();

        fill(0, 180);
        noStroke();

        int zoomLevel = map.getZoomLevel();
        synchronized (visibleDotVertices) {
            for (PVector pos : visibleDotVertices) {
                if (zoomLevel <= 4) {
                    rect(pos.x, pos.y, 4, 4);
                } else {
                    // Draw more expensive representations on higher zoom levels (i.e. when fewer dots)
                    ellipse(pos.x, pos.y, 8, 8);
                }
            }
        }

        fill(255);
        rect(5, 5, 180, 20);
        fill(0);
        text("fps: " + nfs(frameRate, 0, 2) + " (" + visibleDotVertices.size() + " dots)", 10, 20);
    }

    private void mapChanged(MapEvent mapEvent) {
        //println("mapChanged. lastTime:" + lastTime);
        resetCallTimer();
        //callTimer();
    }

    int lastTime = 0;
    int interval = 1000;

    private void resetCallTimer() {
        lastTime = millis();
    }

    private void callTimer() {
        if (millis() - lastTime >= interval) {
            //println("next call!");
            updateDots();
            lastTime = millis();
        }
    }

    private void updateDots() {
        // Check map area only once after user interaction.
        // Additionally, instead of calculating the screen position each frame, store it in new list.
        brLoc = map.getBottomRightBorder();
        tlLoc = map.getTopLeftBorder();
        synchronized (visibleDotVertices) {
            visibleDotVertices.clear();
            for (Dot dot : dots) {
                if (dot.location.getLat() > brLoc.getLat() && dot.location.getLat() < tlLoc.getLat()
                        && dot.location.getLon() > tlLoc.getLon() && dot.location.getLon() < brLoc.getLon()) {
                    PVector pos = map.getScreenPosition(dot.location);
                    visibleDotVertices.add(pos);
                }
            }
        }
    }

    private List<Dot> createRandomDots(int dotNumbers) {
        List<Dot> dots = new ArrayList<>();
        for (int i = 0; i < dotNumbers; i++) {
            Dot dot = new Dot(new Location(random(-85, 85), random(-180, 180)), new Date());
            dots.add(dot);
        }
        return dots;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{MillionDotsMapApp4Timer.class.getName()});
    }
}

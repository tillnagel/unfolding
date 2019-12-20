package de.fhpotsdam.unfolding.overviewdetail;

import de.fhpotsdam.unfolding.examples.overviewdetail.connection.ConvexHull;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Simple app to test convex hull algorithm.
 *
 * Click anywhere to add vertices. The convex hull is updated.
 *
 */
public class ConvexHullApp extends PApplet {

    ConvexHull convexHull;
    
    @Override
    public void settings() {
        size(500, 500);
        smooth();
    }

    @Override
    public void setup() {
        background(255);

        convexHull = new ConvexHull(this);
        convexHull.showDebugPoints = true;
        convexHull.addPoint(new PVector(random(width), random(height)));
        convexHull.addPoint(new PVector(random(width), random(height)));
        convexHull.addPoint(new PVector(random(width), random(height)));
    }

    @Override
    public void draw() {
        background(255);
        convexHull.draw();
    }

    @Override
    public void mousePressed() {
        convexHull.addPoint(new PVector(mouseX, mouseY));
    }

    @Override
    public void keyPressed() {
        if (key == BACKSPACE) {
            convexHull.clearPoints();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{ConvexHullApp.class.getName()});
    }
}

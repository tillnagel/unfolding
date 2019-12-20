package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Tests the generalization / simplification method to reduce vertices in a
 * polyline.
 *
 * Click to add new points, press + and - to decrease or increase tolerance
 * (generalization factor).
 *
 */
public class GeneralizationTestApp extends PApplet {

    UnfoldingMap map;

    List<PVector> points = new ArrayList<>();
    List<PVector> simplifiedPoints = new ArrayList<>();

    float tolerance = 10;

    @Override
    public void settings() {
        size(800, 600, OPENGL);
    }
    
    @Override
    public void setup() {
        // map = new UnfoldingMap(this);
        points.add(new PVector(random(width), random(height)));
        for (int i = 1; i < 10; i++) {
            points.add(PVector.add(points.get(i - 1), new PVector(random(-10, 10), random(-10, 10), 0)));
        }
        simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);
    }

    @Override
    public void draw() {
        background(250);

        simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);

        drawLine(points, color(0, 50), color(255, 0, 0, 20));
        drawLine(simplifiedPoints, color(0), color(255, 0, 0, 100));
    }

    @Override
    public void keyPressed() {
        if (key == '+') {
            tolerance++;
        }
        if (key == '-') {
            tolerance--;
        }
        println(tolerance);
    }

    @Override
    public void mouseClicked() {
        points.add(new PVector(mouseX, mouseY));
    }

    private void drawLine(List<PVector> points, int strokeColor, int color) {
        stroke(strokeColor);
        noFill();
        beginShape();
        for (PVector p : points) {
            vertex(p.x, p.y);
        }
        endShape();

        noStroke();
        fill(color);
        for (PVector p : points) {
            ellipse(p.x, p.y, 5, 5);
        }
    }
    
    public static void main(String args[]) {
        PApplet.main(new String[]{GeneralizationTestApp.class.getName()});
    }
}

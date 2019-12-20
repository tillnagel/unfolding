package de.fhpotsdam.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class CentroidOfCentroidTestApp extends PApplet {

    List<List<PVector>> verticesList = new ArrayList<>();

    List<PVector> allVertices = new ArrayList<>();
    List<PVector> allCentroids = new ArrayList<>();

    @Override
    public void settings() {
        size(800, 600);
        smooth();
    }
    
    @Override
    public void setup() {
        List<PVector> vertices = new ArrayList<>();
        verticesList.add(vertices);
    }

    @Override
    public void draw() {
        background(255);

        allCentroids.clear();
        for (List<PVector> vertices : verticesList) {
            fill(0, 100);
            beginShape();
            for (PVector v : vertices) {
                vertex(v.x, v.y);
            }
            endShape();

            PVector centroid = getCentroidOfPolygon(vertices);
            fill(255, 0, 0);
            ellipse(centroid.x, centroid.y, 10, 10);

            allCentroids.add(centroid);
        }

        PVector allCentroid = getCentroidOfPolygon(allVertices);
        fill(0, 255, 0);
        ellipse(allCentroid.x, allCentroid.y, 10, 10);

        PVector centroidsCentroid = getCentroidOfPolygon(allCentroids);
        fill(0, 0, 255);
        ellipse(centroidsCentroid.x, centroidsCentroid.y, 10, 10);
    }

    @Override
    public void mouseClicked() {
        PVector mousePos = new PVector(mouseX, mouseY);

        List<PVector> lastVertices = verticesList.get(verticesList.size() - 1);
        lastVertices.add(mousePos);

        allVertices.add(mousePos);
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            List<PVector> vertices = new ArrayList<>();
            verticesList.add(vertices);
            println("Added new list. Total now: " + verticesList.size());
        }
    }
    
    private static PVector getCentroidOfPolygon(List<PVector> originalVertices) {
        List<PVector> vertices = getClosedPolygon(originalVertices);
        float cx = 0f, cy = 0f;
        for (int i = 0; i < vertices.size() - 1; i++) {
            PVector vi0 = vertices.get(i);
            PVector vi1 = vertices.get(i + 1);
            cx = cx + (vi0.x + vi1.x) * (vi0.x * vi1.y - vi0.y * vi1.x);
            cy = cy + (vi0.y + vi1.y) * (vi0.x * vi1.y - vi0.y * vi1.x);
        }
        float area = getArea(vertices);
        cx /= (6f * area);
        cy /= (6f * area);
        return new PVector(cx, cy);
    }

    private static List<PVector> getClosedPolygon(List<PVector> originalVertices) {
        if (originalVertices.size() < 1
                || (originalVertices.get(0).equals(originalVertices.get(originalVertices.size() - 1)))) {
            // Return unchanged, if only one point, or already closed
            return originalVertices;
        }

        List<PVector> vertices = new ArrayList<>(originalVertices.size() + 1);
        for (int i = 0; i < originalVertices.size(); i++) {
            vertices.add(new PVector());
        }
        Collections.copy(vertices, originalVertices);
        if (vertices.size() > 1) {
            if (!vertices.get(0).equals(vertices.get(vertices.size() - 1))) {
                // Add first vertex on last position to close polygon
                vertices.add(vertices.get(0));
            }
        }
        return vertices;
    }

    private static float getArea(List<PVector> vertices) {
        float sum = 0;
        for (int i = 0; i < vertices.size() - 1; i++) {
            PVector vi0 = vertices.get(i);
            PVector vi1 = vertices.get(i + 1);
            sum += (vi0.x * vi1.y - vi1.x * vi0.y);
        }
        return sum * 0.5f;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{CentroidOfCentroidTestApp.class.getName()});
    }
}

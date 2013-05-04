package de.fhpotsdam.utils;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class CentroidOfCentroidTestApp extends PApplet {

	List<List<PVector>> verticesList = new ArrayList<List<PVector>>();

	List<PVector> allVertices = new ArrayList<PVector>();
	List<PVector> allCentroids = new ArrayList<PVector>();

	public void setup() {
		size(800, 600);
		smooth();
		
		List<PVector> vertices = new ArrayList<PVector>();
		verticesList.add(vertices);
	}

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

			PVector centroid = InteractiveCentroidTestApp.getCentroidOfPolygon(vertices);
			fill(255, 0, 0);
			ellipse(centroid.x, centroid.y, 10, 10);

			allCentroids.add(centroid);
		}

		PVector allCentroid = InteractiveCentroidTestApp.getCentroidOfPolygon(allVertices);
		fill(0, 255, 0);
		ellipse(allCentroid.x, allCentroid.y, 10, 10);

		PVector centroidsCentroid = InteractiveCentroidTestApp.getCentroidOfPolygon(allCentroids);
		fill(0, 0, 255);
		ellipse(centroidsCentroid.x, centroidsCentroid.y, 10, 10);
	}

	public void mouseClicked() {
		PVector mousePos = new PVector(mouseX, mouseY);

		List<PVector> lastVertices = verticesList.get(verticesList.size() - 1);
		lastVertices.add(mousePos);

		allVertices.add(mousePos);
	}

	public void keyPressed() {
		if (key == ' ') {
			List<PVector> vertices = new ArrayList<PVector>();
			verticesList.add(vertices);
			println("Added new list. Total now: " + verticesList.size());
		}
	}

}

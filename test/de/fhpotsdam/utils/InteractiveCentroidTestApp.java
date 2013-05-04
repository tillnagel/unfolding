package de.fhpotsdam.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class InteractiveCentroidTestApp extends PApplet {

	UnfoldingMap map;
	List<Location> locations = new ArrayList<Location>();

	public void setup() {
		size(800, 600);
		smooth();

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		fill(0);
		List<PVector> vertices = new ArrayList<PVector>();
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			ellipse(pos.x, pos.y, 10, 10);
			vertices.add(pos);
		}

		fill(0, 100);
		beginShape();
		for (PVector pos : vertices) {
			vertex(pos.x, pos.y);
		}
		endShape();

		Location centroid = GeoUtils.getCentroid(locations);
		ScreenPosition centroidPos = map.getScreenPosition(centroid);
		fill(255, 0, 0);
		ellipse(centroidPos.x, centroidPos.y, 10, 10);

		PVector centroidPos1 = getCentroidOfPolygon(vertices);
		fill(0, 255, 0);
		ellipse(centroidPos1.x, centroidPos1.y, 10, 10);
	}

	public void mouseClicked() {
		Location location = map.getLocation(mouseX, mouseY);
		locations.add(location);
	}

	public void keyPressed() {
		println(locations);
	}

	public static PVector getCentroidOfPolygon(List<PVector> originalVertices) {
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

	public static List<PVector> getClosedPolygon(List<PVector> originalVertices) {
		if (originalVertices.size() < 1
				|| (originalVertices.get(0).equals(originalVertices.get(originalVertices.size() - 1)))) {
			// Return unchanged, if only one point, or already closed
			return originalVertices;
		}

		List<PVector> vertices = new ArrayList<PVector>(originalVertices.size() + 1);
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

	public static float getArea(List<PVector> vertices) {
		float sum = 0;
		for (int i = 0; i < vertices.size() - 1; i++) {
			PVector vi0 = vertices.get(i);
			PVector vi1 = vertices.get(i + 1);
			sum += (vi0.x * vi1.y - vi1.x * vi0.y);
		}
		return sum * 0.5f;
	}

}

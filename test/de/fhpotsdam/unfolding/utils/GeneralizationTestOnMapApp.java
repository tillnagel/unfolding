package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;

/**
 * Tests the generalization / simplification method to reduce polylines on a map.
 * 
 * Click to add new points, press + and - to decrease or increase tolerance (generalization factor).
 * 
 */
public class GeneralizationTestOnMapApp extends PApplet {

	UnfoldingMap map;

	List<Location> locations = new ArrayList<Location>();

	List<PVector> points = new ArrayList<PVector>();
	List<PVector> simplifiedPoints = new ArrayList<PVector>();

	float tolerance = 4;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new StamenMapProvider.Toner());
		// MapUtils.createDefaultEventDispatcher(this, map);

		EventDispatcher eventDispatcher = new EventDispatcher();
		MouseHandler mouseHandler = new MouseHandler(this, map);
		eventDispatcher.addBroadcaster(mouseHandler);
		eventDispatcher.register(map, PanMapEvent.TYPE_PAN, map.getId());
		eventDispatcher.register(map, ZoomMapEvent.TYPE_ZOOM, map.getId());

	}

	public void draw() {
		background(250);

		map.draw();

		points = new ArrayList<PVector>();
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			points.add(pos);
		}
		if (!points.isEmpty()) {
			// drawLine(points, color(0, 50), color(255, 0, 0, 20));
			simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);
			drawLine(simplifiedPoints, color(0, 200), color(255, 0, 0, 200));
		}
	}

	public void drawLine(List<PVector> points, int strokeColor, int color) {
		stroke(strokeColor);
		strokeWeight(2);
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

	public void keyPressed() {
		if (key == '+') {
			tolerance++;
		}
		if (key == '-') {
			tolerance--;
		}
		// simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);
		println(tolerance);
	}

	public void mouseClicked() {
		locations.add(map.getLocation(mouseX, mouseY));
	}
}

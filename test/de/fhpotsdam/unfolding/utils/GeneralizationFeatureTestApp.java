package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;

/**
 * Multi countries generalization tests
 * 
 */
public class GeneralizationFeatureTestApp extends PApplet {

	UnfoldingMap map;
	List<Feature> countries;

	float tolerance = 1;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new StamenMapProvider.Toner());
		MapUtils.createDefaultEventDispatcher(this, map);

		countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
	}

	public void draw() {
		background(250);

		map.draw();

		for (Feature country : countries) {
			List<PVector> simplifiedPoints = simplifyFeature(country);
			drawLine(simplifiedPoints, color(255, 0, 0, 200));
		}

	}

	public List<PVector> simplifyFeature(Feature feature) {
		List<PVector> simplifiedPoints = new ArrayList<PVector>();
		if (feature instanceof ShapeFeature) {
			ShapeFeature shapeFeature = (ShapeFeature) feature;

			List<PVector> points = getPositions(shapeFeature.getLocations());
			simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);
		}
		return simplifiedPoints;
	}

	public List<PVector> getPositions(List<Location> locations) {
		List<PVector> points = new ArrayList<PVector>();
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			points.add(pos);
		}
		return points;
	}

	public void drawLine(List<PVector> points, int strokeColor) {
		stroke(strokeColor);
		strokeWeight(1);
		noFill();
		beginShape();
		for (PVector p : points) {
			vertex(p.x, p.y);
		}
		endShape();
	}

	public void keyPressed() {
		if (key == 'T') {
			tolerance++;
		}
		if (key == 't') {
			tolerance--;
		}
		// simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);
		println(tolerance);
	}

	public void mouseClicked() {
		// locations.add(map.getLocation(mouseX, mouseY));
	}
}

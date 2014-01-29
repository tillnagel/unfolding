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
 * Single country generalization test.
 * 
 */
public class GeneralizationPolyMarkerTestApp extends PApplet {

	UnfoldingMap map;

	ShapeFeature selectedCountry;

	float tolerance = 1;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new StamenMapProvider.Toner());
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		for (Feature country : countries) {
			if ("Germany".equals(country.getStringProperty("name"))) {
				selectedCountry = (ShapeFeature) country;
			}
		}
	}

	public void draw() {
		background(250);

		map.draw();

		List<PVector> points = new ArrayList<PVector>();
		for (Location location : selectedCountry.getLocations()) {
			ScreenPosition pos = map.getScreenPosition(location);
			points.add(pos);
		}
		if (!points.isEmpty()) {
			List<PVector> simplifiedPoints = GeneralizationUtils.simplify(points, tolerance, true);
			drawLine(simplifiedPoints, color(255, 0, 0, 200), color(255, 0, 0, 200));
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
			ellipse(p.x, p.y, 2, 2);
		}
	}

	public void keyPressed() {
		if (key == 'T') {
			tolerance++;
		}
		if (key == 't') {
			tolerance--;
		}
		println(tolerance);
	}

}

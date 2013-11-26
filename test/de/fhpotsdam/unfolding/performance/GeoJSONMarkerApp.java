package de.fhpotsdam.unfolding.performance;

import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays countries of the world as simple polygons.
 * 
 * Reads from a GeoJSON file, and uses default marker creation. Features are polygons.
 * 
 * Press SPACE to toggle visibility of the polygons.
 */
public class GeoJSONMarkerApp extends PApplet {

	UnfoldingMap map;

	List<Feature> countries;
	PShape shapeGroup;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		map = new UnfoldingMap(this, 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		shapeGroup = createShapeGroup(countries);
	}

	public void draw() {
		background(160);
		map.draw();
		
		shape(shapeGroup);

		fill(255);
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2), 10, 20);
	}

	public PShape createShapeGroup(List<Feature> features) {
		shapeGroup = createShape(PShape.GROUP);

		for (int i = 0; i < features.size(); i++) {
			Feature feature = features.get(i);
			if (feature.getType().equals(FeatureType.POLYGON)) {
				ShapeFeature shapeFeature = (ShapeFeature) feature;

				updateShape("c" + i, shapeFeature, false);
			}
		}
		return shapeGroup;
	}

	public void updateShape(String shapeName, ShapeFeature shapeFeature, boolean update) {
		if (!update) {
			// Create shape
			PShape shape = createShape();
			shape.beginShape();
			shape.stroke(30);
			//shape.fill(255, 0, 0, 4);
			shape.fill(color(255, 0, 0), 100);
			updateShapeVertices(shape, shapeFeature, false);
			shape.endShape();

			// Add shape AND add to name list to retrieve later
			shapeGroup.addChild(shape);
			shapeGroup.addName(shapeName, shape);
		}
		else {
			// Get shape via name list (and not via index)
			PShape shape = shapeGroup.getChild(shapeName);
			// PShape shape = shapeGroup.getChild(i);

			updateShapeVertices(shape, shapeFeature, true);
		}
	}

	public void updateShapeVertices(PShape shape, ShapeFeature shapeFeature, boolean update) {
		List<Location> locations = shapeFeature.getLocations();
		int v = 0;
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			if (update) {
				shape.setVertex(v++, pos.x, pos.y);
			}
			else {
				shape.vertex(pos.x, pos.y);
			}
		}
	}

	public void mapChanged(MapEvent mapEvent) {
		updateFeatures(countries);
	}

	public void updateFeatures(List<Feature> features) {
		for (int i = 0; i < features.size(); i++) {
			Feature feature = features.get(i);
			if (feature.getType().equals(FeatureType.POLYGON)) {
				ShapeFeature shapeFeature = (ShapeFeature) feature;
				
				updateShape("c" + i, shapeFeature, true);
			}
		}

	}
}

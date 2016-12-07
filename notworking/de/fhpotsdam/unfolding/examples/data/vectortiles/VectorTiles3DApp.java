package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class VectorTiles3DApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;

	VectorTilesUtils vectorTilesUtils;
	/**
	 * Name of the features layer (in OpenStreetMap).
	 */
	String buildingsLayer = "buildings,roads";
	List<Feature> features;
	List<Feature> polygons;
	boolean loadUniqueMarkers = true;

	public void settings() {
		size(1500, 800, "processing.opengl.PGraphics3D");
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { VectorTiles3DApp.class.getName() });
	}

	public void setup() {
		frameRate(60);
		
		map = new UnfoldingMap(this, "myMap");
		// map.zoomAndPanTo(16, new Location(52.501, 13.395));
		map.zoomAndPanTo(13, new Location(37.782281f, -122.409531f));
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);

		vectorTilesUtils = new VectorTilesUtils(this, map);

		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(buildingsLayer, width / 2, height / 2);
		map.addMarkers(markers);
		System.out.println(markers.get(0).getStringProperty("height"));
		// for (Marker m:markers)
		// System.out.println(m.getLocation()+" "+m.getProperties());
		features = (vectorTilesUtils.loadFeaturesForScreenPos(buildingsLayer, width / 2, height / 2));

		// List<SimplePolygonMarker> polyMarkers=new ArrayList<>();
		// for (Feature feature : features) {
		// if (feature.getType()==(Feature.FeatureType.POLYGON)) {
		// ShapeFeature b = (ShapeFeature) feature;
		// // if (feature.getProperty("height") != null) {
		// // System.out.println(feature.getProperty("height") + " " + ((ShapeFeature) feature).getLocations());
		// // System.out.println(Arrays.toString(GeoUtils.getBoundingBox(((ShapeFeature) feature).getLocations())));
		// HashMap<String, Object> properties = new HashMap<>();
		// properties.put("height", feature.getProperty("height")==null?100:feature.getProperty("height"));
		// SimplePolygonMarker m = new SimplePolygonMarker(((ShapeFeature) feature).getLocations(), properties);
		// // m.setProperty("height",new Integer(432));
		// polyMarkers.add(m);
		// map.addMarker(m);
		//
		// // }
		//
		// }
		// }
		polygons = new ArrayList<Feature>();
		for (Feature f : features) {
			if (f.getType().equals(Feature.FeatureType.POLYGON)) {
				polygons.add(f);
			}
		}

	}

	float rotateX = 0.9f;
	float rotateZ = (float) 0;
	float rotateVelocityZ = 0.003f;
	
	public void draw() {
		background(100);
		
		pushMatrix();
		// translate(0, 0, -100);
		// rotateX(radians(40));
		
		translate(width / 2, height / 3, 0);
		rotateX(rotateX);
		rotateZ(rotateZ);
		translate(-map.getWidth() / 2, -map.getHeight() / 2);
		
		map.draw();
		for (Feature feature : polygons) {
			if (feature.getType() == Feature.FeatureType.POLYGON) {
				drawBuilding(feature);
			}
		}
		
		rotateZ += rotateVelocityZ;
		popMatrix();
		
		debugDisplay.draw();
		map.getZoomLevel();
	}

	public void mouseClicked() {
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(buildingsLayer, mouseX, mouseY);
		List<Feature> features = (vectorTilesUtils.loadFeaturesForScreenPos(buildingsLayer, width / 2, height / 2));
		addMarkers(markers, loadUniqueMarkers);
		polygons.addAll(features);
	}

	public void keyPressed() {
		if (key == 'u') {
			loadUniqueMarkers = !loadUniqueMarkers;
		}
		if (key == ' ') {
			map.getDefaultMarkerManager().clearMarkers();
		}
		if (key == 'a') {
			List<Marker> markers = vectorTilesUtils.loadMarkersForCurrentMapView(buildingsLayer);
			addMarkers(markers, loadUniqueMarkers);
		}
	}

	/*
	 * @param unique Indicates whether to check for same markers only loaded. If true only new markers are returned, if
	 * false all markers containing possible duplicates.
	 */

	public void drawBuilding(Feature feature) {
		ShapeFeature building = (ShapeFeature) feature;
		float height = building.getProperty("height") == null ? 20 : ((Double) building.getProperty("height"))
				.floatValue();
		// float height=20;
		// System.out.println(height);
		for (int i = 0; i < building.getLocations().size() - 1; i++) {
			ScreenPosition pos = map.getScreenPosition(building.getLocations().get(i));
			Vec3D first = new Vec3D(pos.x, pos.y, 0);
			Vec3D firstTop = new Vec3D(first.x, first.y, height);
			ScreenPosition pos2 = map.getScreenPosition(building.getLocations().get(i + 1));
			// ScreenPosition pos3 = map.getScreenPosition(building.getLocations().get(i1));
			Vec3D second = new Vec3D(pos2.x, pos2.y, 0);
			Vec3D secondTop = new Vec3D(second.x, second.y, height);
			// System.out.println(first);

			pushStyle();
			fill(0, 100, 100);
			// pa.noStroke();
			// pa.noFill();
			stroke(0);
			beginShape();
			vertex(first.x, first.y, first.z);
			vertex(firstTop.x, firstTop.y, firstTop.z);
			vertex(secondTop.x, secondTop.y, secondTop.z);
			vertex(second.x, second.y, second.z);
			endShape();
			popStyle();

		}

	}

	public void addMarkers(List<Marker> markers, boolean unique) {
		if (unique) {
			// Add only new markers
			addUniqueMarkers(markers);
		} else {
			// Add all markers
			map.addMarkers(markers);
		}
	}

	// TODO Move addUniqueMarkers to UnfoldingMap.
	public void addUniqueMarkers(List<Marker> markers) {
		MarkerManager<Marker> markerManager = map.getDefaultMarkerManager();
		int newMarkerCount = 0;
		int oldMarkerCount = 0;
		for (Marker marker : markers) {
			if (markerManager.findMarkerById(marker.getId()) == null) {
				markerManager.addMarker(marker);
				newMarkerCount++;
			} else {
				oldMarkerCount++;
			}
		}
		println("Added " + newMarkerCount + " new markers, and omitted " + oldMarkerCount
				+ " previously loaded markers.");
	}

}

package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Shows a mask for the map with buildings drawn behind the mask. Click to select a building which will be drawn in
 * green and before the mask.
 * 
 */
public class SelectedBuildingsMaskApp extends PApplet {

	UnfoldingMap map;
	MapDisplayShader mapDisplayShader;

	VectorTilesUtils vectorTilesUtils;
	String featureLayer = "buildings";

	Marker selectedMarker = null;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SelectedBuildingsMaskApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		map.setZoomRange(10, 19);

		vectorTilesUtils = new VectorTilesUtils(this, map, VectorTilesApp.MAPZEN_API_KEY);
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, width / 2, height / 2);
		map.addMarkers(markers);

		PImage maskImage = loadImage("shader/mask-circular.png");
		mapDisplayShader = new MaskedMapDisplayShader(this, 400, 400, maskImage);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
	}

	public void draw() {
		background(0);
		map.draw();
		g.resetShader();

		if (selectedMarker != null) {
			SimplePolygonMarker polygonMarker = (SimplePolygonMarker) selectedMarker;
			strokeWeight(1);
			stroke(28, 102, 120);
			fill(116, 188, 157);
			beginShape();
			for (Location location : polygonMarker.getLocations()) {
				ScreenPosition pos = map.getScreenPosition(location);
				vertex(pos.x, pos.y);
			}
			endShape();
		}
	}

	public void keyPressed() {
		if (key == 'l') {
			List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, mouseX, mouseY);
			map.addMarkers(markers);
		}
	}

	public void mouseClicked() {
		List<Marker> markers = map.getMarkers();
		for (Marker marker : markers)
			marker.setHidden(true);

		selectedMarker = map.getFirstHitMarker(mouseX, mouseY);
	}

}

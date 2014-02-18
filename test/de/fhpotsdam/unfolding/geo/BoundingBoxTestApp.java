package de.fhpotsdam.unfolding.geo;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class BoundingBoxTestApp extends PApplet {

	UnfoldingMap map;

	Location[] boundingBox = null;
	Location center = null;
	Location centerEuclidean = null;
	Location centerBoundingBox = null;

	public void setup() {
		size(800, 800, OPENGL);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countryFeatures = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countryFeatures);
		map.addMarkers(countryMarkers);
	}

	public void draw() {
		background(240);
		map.draw();

		if (boundingBox != null) {
			ScreenPosition nwPos = map.getScreenPosition(boundingBox[0]);
			ScreenPosition sePos = map.getScreenPosition(boundingBox[1]);
			stroke(0, 255, 0, 200);
			noFill();
			rect(nwPos.x, nwPos.y, sePos.x - nwPos.x, sePos.y - nwPos.y);
		}

		noStroke();
		if (center != null) {
			ScreenPosition posC = map.getScreenPosition(center);
			fill(0, 0, 255);
			ellipse(posC.x, posC.y, 6, 6);
		}

		if (centerEuclidean != null) {
			ScreenPosition posCE = map.getScreenPosition(centerEuclidean);
			fill(0, 255, 255);
			ellipse(posCE.x, posCE.y, 6, 6);
		}
		if (centerBoundingBox != null) {
			ScreenPosition posBB = map.getScreenPosition(centerBoundingBox);
			fill(0, 255, 0);
			ellipse(posBB.x, posBB.y, 6, 6);
		}
	}

	public void mouseMoved() {
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			centerEuclidean = GeoUtils.getEuclideanCentroid(GeoUtils.getLocations(marker));
			center = GeoUtils.getCentroid(marker);

			List<Location> boundingBoxLocations = Arrays.asList(GeoUtils.getBoundingBox(GeoUtils.getLocations(marker)));
			centerBoundingBox = GeoUtils.getEuclideanCentroid(boundingBoxLocations);
		}
	}

	public void mouseClicked() {
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			boundingBox = GeoUtils.getBoundingBox(GeoUtils.getLocations(marker));
			map.zoomAndPanToFit(GeoUtils.getLocations(marker));
		} else {
			boundingBox = null;
			map.zoomAndPanTo(2, new Location(0, 0));
		}
	}

}

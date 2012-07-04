package de.fhpotsdam.unfolding.examples.marker.imagemarker;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class ImageMarkerApp extends PApplet {

	Location berlinLocation = new Location(52.5f, 13.4f);
	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		ImageMarker imgMarker = new ImageMarker(berlinLocation, loadImage("marker_red.png"));
		map.addMarkers(imgMarker);
	}

	public void draw() {
		map.draw();
	}

}

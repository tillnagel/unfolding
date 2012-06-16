package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example with three markers, managed by the MarkerManager. The markers are cut-off at the border of the map.
 */
public class SimpleMarkerManagerApp extends PApplet {

	Map map;

	Location berlinLocation = new Location(52.5f, 13.4f);
	Location mexicoCityLocation = new Location(19.4f, -99.1f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, -42f));
		MapUtils.createDefaultEventDispatcher(this, map);

		SimpleMarker berlinMarker = new SimpleMarker(berlinLocation);
		berlinMarker.setColor(0, 200, 0, 100);
		berlinMarker.setRadius(4);
		SimpleMarker mexicoCityMarker = new SimpleMarker(mexicoCityLocation);
		mexicoCityMarker.setColor(200, 0, 0, 100);
		SimpleLinesMarker connectionMarker = new SimpleLinesMarker(berlinLocation, mexicoCityLocation);
		map.addMarkers(berlinMarker, mexicoCityMarker, connectionMarker);
	}

	public void draw() {
		background(240);
		map.draw();
	}

}

package de.fhpotsdam.unfolding.examples.marker.compare;

import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.examples.SimpleMapApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class NonMarkerExample extends PApplet {

	Map map;
	
	Location locationBerlin;
	Location locationLondon;
	
	public void setup() {
		size(800,400,GLConstants.GLGRAPHICS);
		
		map = new Map(this);
		map.setTweening(true);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		locationBerlin = new Location(52.5f, 13.4f);
		locationLondon = new Location(51.5f, 0f);
	}
	

	public void draw() {
		background(70);
		
		map.draw();

		// Draws locations on screen positions according to their geo-locations.
		
		// Fixed-size marker
		float xyBerlin[] = map.getScreenPositionFromLocation(locationBerlin);
		fill(0, 200, 0, 100);
		ellipse(xyBerlin[0], xyBerlin[1], 20, 20);
		
		// Zoom dependent marker size
		float xyLondon[] = map.getScreenPositionFromLocation(locationLondon);
		fill(200, 0, 0, 100);
		float s = map.getZoom();
		ellipse(xyLondon[0], xyLondon[1], s, s);
	}
}

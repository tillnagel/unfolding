package de.fhpotsdam.unfolding.examples.marker.compare;

import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.examples.SimpleMapApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import processing.core.PVector;

@SuppressWarnings("serial")
public class SimpleMarkerExample extends PApplet {

	Map map;
	
	SimpleMarker markerBerlin;
	SimpleMarker markerLondon;
	
	public void setup() {
		size(800,400,GLConstants.GLGRAPHICS);
		
		map = new Map(this);
		map.setTweening(true);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		markerBerlin = new SimpleMarker();//TODO const
		markerBerlin.location = new Location(52.5f, 13.4f);
		
		markerLondon = new SimpleMarker();
		markerLondon.location = new Location(51.5f, 0f);
	}
	

	public void draw() {
		background(70);
		
		map.draw();

		// Draws locations on screen positions according to their geo-locations.
		
		// Fixed-size marker
		ScreenPosition posBerlin = markerBerlin.getScreenPosition(map);
//				map.getScreenPositionFromLocation(markerBerlin.getLocation());
		fill(0, 200, 0, 100);
		ellipse(posBerlin.x, posBerlin.y, 20, 20);
		//REVISIT
//		markerBerlin.drawNOW(map);
		
		// Zoom dependent marker size
		ScreenPosition posLondon = markerLondon.getScreenPosition(map);
		fill(200, 0, 0, 100);
		float s = map.getZoom();
		ellipse(posLondon.x, posLondon.y, s, s);
	}
}

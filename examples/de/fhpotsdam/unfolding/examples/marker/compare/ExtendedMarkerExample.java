package de.fhpotsdam.unfolding.examples.marker.compare;

import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.examples.SimpleMapApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PVector;

@SuppressWarnings("serial")
public class ExtendedMarkerExample extends PApplet {

	Map map;
	
	SimpleMarker markerBerlin;
	RectangularMarker markerLondon;
	
	public void setup() {
		size(800,400,GLConstants.GLGRAPHICS);
		
		map = new Map(this);
		map.setTweening(true);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		markerBerlin = new SimpleMarker();
		markerBerlin.location = new Location(52.5f, 13.4f);
		markerBerlin.setColor(0, 200, 0, 100);
		markerBerlin.setRadius(2);
		
		markerLondon = new RectangularMarker();
		markerLondon.location = new Location(51.5f, 0f);
		markerLondon.setColor(200, 0, 0, 100);
		markerLondon.setRadius(2);
		
		map.addMarker(markerBerlin);
		map.addMarker(markerLondon);
	}
	

	public void draw() {
		background(70);
		
		map.draw();
	}
}

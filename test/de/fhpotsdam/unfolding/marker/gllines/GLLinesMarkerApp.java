package de.fhpotsdam.unfolding.marker.gllines;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

@SuppressWarnings("serial")
public class GLLinesMarkerApp extends PApplet {

	UnfoldingMap map;
	
	//TODO @fx-lange use dataset
	
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, 0, 0, 800, 500);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);

		GLLinesMarker linesMarker = new GLLinesMarker();
		linesMarker.addLocations(new Location(52.5f, 13.4f+0.5f));
		linesMarker.addLocations(new Location(51.5f, 0.0f));
		linesMarker.addLocations(new Location(48.f, 5f));
		
		map.addMarkers(linesMarker);
	}
	
	public void draw(){
		map.draw();
	}
}

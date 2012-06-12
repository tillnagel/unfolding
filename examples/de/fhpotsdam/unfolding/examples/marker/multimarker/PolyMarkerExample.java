package de.fhpotsdam.unfolding.examples.marker.multimarker;

import javax.swing.text.html.HTMLEditorKit.Parser;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.MapUtils;

@SuppressWarnings("serial")
public class PolyMarkerExample extends PApplet {

	Map map;
	
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, 0, 0, 800, 500);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, 8f));
		MapUtils.createDefaultEventDispatcher(this, map);

		GLLinesMarker area = new GLLinesMarker();
		area.addLocations(new Location(52.5f, 13.4f+0.5f));
		area.addLocations(new Location(51.5f, 0.0f));
		area.addLocations(new Location(48.f, 5f));
		area.addLocations(new Location(52.5f, 13.4f+0.5f));
		
		map.addMarkers(area);
		
//		MarkerManager<Marker> fluesse;
//		MarkerManager<Marker> strassen;
//		
//		map.addMarkerManager(fluesse);
//		map.addMarkerManager(strassen);
		
//		map.moveLayer(flusse,-1);
		
//		flugParser.pasrseTo(mm,color);
//		mm.setColor();
		
		//mouse
//		fluesse.enableDrawing();
//		fluesse.disableDrawing();
	}
	
	public void draw(){
		map.draw();
	}
}

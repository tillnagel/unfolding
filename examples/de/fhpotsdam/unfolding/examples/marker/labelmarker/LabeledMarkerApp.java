package de.fhpotsdam.unfolding.examples.marker.labelmarker;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * 
 */
public class LabeledMarkerApp extends PApplet {

	UnfoldingMap map;

	Location berlinLocation = new Location(52.5f, 13.4f);
	LabeledMarker berlinMarker;
	PFont font;
	
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		// size(800, 600);

		map = new UnfoldingMap(this, "map", 50, 50, 700, 500);
		map.zoomToLevel(3);
		map.panTo(berlinLocation);
		MapUtils.createDefaultEventDispatcher(this, map);

		font = loadFont("Helvetica-16.vlw");
		berlinMarker = new LabeledMarker(berlinLocation, "Fossils", font, 15);
		map.addMarkers(berlinMarker);
	}

	public void draw() {
		background(240);
		map.draw();
		
		
	}

	public void mouseMoved() {
		// Simplest method to check for hit test.
		if (berlinMarker.isInside(map, mouseX, mouseY)) {
			berlinMarker.setSelected(true);
		} else {
			berlinMarker.setSelected(false);
		}
	}

}

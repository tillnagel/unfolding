package de.fhpotsdam.unfolding.rendering;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
*/
public class RenderMarkerTestApp extends PApplet {

	UnfoldingMap map;
	SimplePointMarker berlinMarker;

	public void setup() {
		size(800, 600, OPENGL);
		//size(800, 600);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		map.panTo(new Location(40f, -98f));
		MapUtils.createDefaultEventDispatcher(this, map);

		Location berlinLocation = new Location(40f, -98f);
		berlinMarker = new TestMarker(berlinLocation);
		berlinMarker.setColor(color(255, 0, 0, 100));
		berlinMarker.setStrokeColor(color(255, 0, 0));
		berlinMarker.setStrokeWeight(2);
		map.addMarkers(berlinMarker);
	}

	public void draw() {
		background(240);
		map.draw();

		ScreenPosition pos = berlinMarker.getScreenPosition(map);
		fill(255, 0, 0, 100);
		stroke(255, 0, 0);
		strokeWeight(2);
		ellipse(pos.x, pos.y + 30, 20, 20);
		println("o:" + pos.x + ", " + pos.y);
		fill(0);
		text("Xylophon", pos.x + 20, pos.y + 30);
	}

}

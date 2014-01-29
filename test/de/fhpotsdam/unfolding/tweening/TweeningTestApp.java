package de.fhpotsdam.unfolding.tweening;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Tests tweening.
 * 
 */
public class TweeningTestApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;

	public void setup() {
		size(1024, 768, OPENGL);

		map = new UnfoldingMap(this);
		map.setTweening(true);
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}

	public void keyPressed() {
		if (key == ' ') {
			println("tweening is " + map.isTweening());
			println("zoom: " + map.getZoomLevel() + ", innerScale: " + map.getZoom());

			map.switchTweening();
		}

		if (key == 'p') {

			Location berlinLocation = new Location(52.51861f, 13.408056f);
			int berlinZoomLevel = 10;
			map.zoomAndPanTo(berlinLocation, berlinZoomLevel);
			// map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

			// map.zoomAndPanTo(new Location(52.2f, 13.2f), 10);
		}
	}
}

package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
public class SimpleMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
	}
}

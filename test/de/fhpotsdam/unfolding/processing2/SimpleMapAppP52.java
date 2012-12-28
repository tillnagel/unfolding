package de.fhpotsdam.unfolding.processing2;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
@SuppressWarnings("serial")
public class SimpleMapAppP52 extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(1024, 768, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
//		MapUtils.createDefaultEventDispatcher(this, map);
		/*
		 * Already called beginDraw()
		-> https://forum.processing.org/topic/already-called-begindraw-for-another-pgraphicsopengl-object-14-9-2012
		 */
	}

	public void draw() {
		map.draw();
		fill(150);
		rect(15, 15, 50, 50);
	}

	public void keyPressed() {
		map.panBy(5, -5);
	}
	
}

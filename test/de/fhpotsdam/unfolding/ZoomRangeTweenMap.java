package de.fhpotsdam.unfolding;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Combines tween-map with zoom restriction. Mark Hester sent that as a bug.
 * 
 * If tweening is on and zoom is restricted, users can zoom further anyway (or it jumps around).
 *
 */
public class ZoomRangeTweenMap extends PApplet {

	UnfoldingMap map;
	Location loc1 = new Location(50, 10);
	Location loc2 = new Location(40, 20);
	
	int minZoomLevel = 8;
	int maxZoomLevel = 11;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		map.setZoomRange(minZoomLevel, maxZoomLevel);
		
		map.setTweening(true);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		
//		if (map.getZoomLevel() < minZoomLevel) {
//			map.zoomToLevel(minZoomLevel);
//		}
//		else if (map.getZoomLevel() > maxZoomLevel) {
//			map.zoomToLevel(maxZoomLevel);
//		}
		
		map.draw();
	}


}

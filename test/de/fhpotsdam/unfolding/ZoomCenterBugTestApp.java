package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class ZoomCenterBugTestApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, P2D);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(10, new Location(52.52, 13.41));
	}

	public void draw() {
		map.draw();
	}

	public void keyPressed() {
		map.mapDisplay.setInnerTransformationCenter(new PVector(width / 2, height / 2));
		if (key == 'z') {
			map.zoomToLevel(12);
		}
		if (key == 't') {
			map.zoomIn();
		}

	}

}

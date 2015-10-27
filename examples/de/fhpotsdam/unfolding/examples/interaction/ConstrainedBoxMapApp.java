package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Manually constrains the map to a rectangular area. Users can only pan within this specified box.
 * 
 * See {@link ConstrainedMapApp} for Unfolding's built-in (but radial) constraint methods.
 */
public class ConstrainedBoxMapApp extends PApplet {

	UnfoldingMap map;

	Location boundTopLeft = new Location(52.8, 12.6);
	Location boundBottomRight = new Location(52.0, 14.5);

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(10, new Location(52.5, 13.4f));
		map.setZoomRange(10, 12);
		map.setTweening(true);
		map.setRectangularPanningRestriction(boundTopLeft, boundBottomRight);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public void keyPressed() {
		if (key == ' ') {
			map.resetPanningRestriction();
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { ConstrainedBoxMapApp.class.getName() });
	}
}

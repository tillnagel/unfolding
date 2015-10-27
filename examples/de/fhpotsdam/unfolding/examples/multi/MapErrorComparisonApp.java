package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Overlays two different map areas depicting the same earth areas. Apparently some satellite images must have been
 * inserted at the wrong position. This is on Microsoft Satellite, as well as on Google Satellite.
 * 
 * <p>
 * ctrl + mouse move to change the alpha level of the top map. Press 'a' and 'd' to rotate (in order to align both
 * maps.)
 * </p>
 */
public class MapErrorComparisonApp extends PApplet {

	UnfoldingMap map1;
	UnfoldingMap map2;
	float fadeValue = 100;
	
	public void settings() {
		size(800, 600, P2D);
	}
	
	public void setup() {
		map1 = new UnfoldingMap(this, "map1", new Microsoft.AerialProvider());
		map2 = new UnfoldingMap(this, "map2", new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map1, map2);

		map1.zoomAndPanTo(13, new Location(56.27415, 130.97737));
		map2.zoomAndPanTo(13, new Location(55.99591, 131.3102));
	}

	public void draw() {
		background(0);

		tint(255);
		map1.draw();
		tint(255, fadeValue);
		map2.draw();
	}

	public void mouseMoved() {
		if (keyPressed && key == CODED && keyCode == CONTROL) {
			float d = (float) mouseX / (float) width;
			fadeValue = map(d, 0, 1, 0, 255);
		}
	}

	public void keyPressed() {
		if (key == 'a') {
			map2.rotate(0.02f);
		}
		if (key == 'd') {
			map2.rotate(-0.02f);
		}

	}
}

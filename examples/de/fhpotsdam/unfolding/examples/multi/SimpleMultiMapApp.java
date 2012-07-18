package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Shows two independent maps.
 * 
 * Press + or - to zoom one of the maps, depending on which you hover the mouse.
 */
public class SimpleMultiMapApp extends PApplet {

	UnfoldingMap map1;
	UnfoldingMap map2;

	public void setup() {
		size(500, 590, GLConstants.GLGRAPHICS);

		map1 = new UnfoldingMap(this, "map1", 0, 0, 500, 290);
		map2 = new UnfoldingMap(this, "map2", 0, 300, 500, 290);
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
	}

	public void keyPressed() {
		UnfoldingMap map = null;
		if (map1.isHit(mouseX, mouseY)) {
			map = map1;
		} else if (map2.isHit(mouseX, mouseY)) {
			map = map2;
		}

		if (map != null) {
			if (key == '+') {
				map.zoomLevelIn();
			}
			if (key == '-') {
				map.zoomLevelOut();
			}
		}
	}

}

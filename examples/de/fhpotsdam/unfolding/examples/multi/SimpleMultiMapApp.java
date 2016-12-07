package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows two independent maps aside each other.
 * 
 * Press + or - to zoom one of the maps, depending on which you hover the mouse.
 */
public class SimpleMultiMapApp extends PApplet {

	UnfoldingMap map1;
	UnfoldingMap map2;
	
	public void settings() {
		size(620, 300, P2D);
	}
	
	public void setup() {
		map1 = new UnfoldingMap(this, "map1", 0, 0, 305, 300);
		map2 = new UnfoldingMap(this, "map2", 315, 0, 305, 300);
		MapUtils.createDefaultEventDispatcher(this, map1, map2);
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

	public static void main(String[] args) {
		PApplet.main(new String[] { SimpleMultiMapApp.class.getName() });
	}
}

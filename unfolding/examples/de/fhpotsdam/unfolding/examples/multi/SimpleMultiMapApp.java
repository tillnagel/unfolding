package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;

public class SimpleMultiMapApp extends PApplet {

	Map map1;
	Map map2;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map1 = new Map(this, "map1", 0, 0, 300, 290);
		map2 = new Map(this, "map2", 0, 300, 300, 290);
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
	}

	public void keyPressed() {
		Map map = null;
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

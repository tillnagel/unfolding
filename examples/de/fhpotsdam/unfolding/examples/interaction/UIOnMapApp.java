package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class UIOnMapApp extends PApplet {

	UnfoldingMap map;
	float uiX = 20, uiY = 20, uiWidth = 300, uiHeight = 60;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();

		fill(0, 200);
		rect(uiX, uiY, uiWidth, uiHeight);
	}

	public void mouseDragged() {
		if (isOverUI(mouseX, mouseY)) {
			// react to UI buttons or whatnot
		}
		else {
			map.panBy(mouseX - pmouseX, mouseY - pmouseY);
		}
	}

	public boolean isOverUI(float x, float y) {
		return x > uiX && x < uiX + uiWidth && y > uiY && y < uiY + height;
	}

}

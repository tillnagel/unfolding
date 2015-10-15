package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Map will be resized when user drags the resize handle.
 * 
 * Drag the window frame (depending on your OS) to resize, or press SPACE to resize randomly.
 * 
 * The map will be resized, but everything else stays the same, i.e. even the current transformation center will be
 * consistent. If the frame size was increased, the missing tiles will be loaded, and added automatically.
 */
public class ResizableMapApp extends PApplet {

	UnfoldingMap map;

	float oldWidth;
	float oldHeight;

	boolean resizingStressTest = false;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		surface.setResizable(true);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		MapUtils.createDefaultEventDispatcher(this, map);

		oldWidth = width;
		oldHeight = height;
	}

	public void draw() {
		if (width != oldWidth || height != oldHeight) {
			map.mapDisplay.resize(width, height);

			oldWidth = width;
			oldHeight = height;
		}

		background(0);
		map.draw();

		if (resizingStressTest) {
			surface.setSize(round(random(500, 900)), round(random(500, 700)));
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { ResizableMapApp.class.getName() });
	}

	public void keyPressed() {
		// Stress test to verify https://github.com/tillnagel/unfolding/issues/107 is fixed
		if (key == ' ') {
			resizingStressTest = !resizingStressTest;
		}
		if (key == 'r') {
			surface.setSize((int) random(displayWidth), (int) random(displayHeight));
		}
	}
}

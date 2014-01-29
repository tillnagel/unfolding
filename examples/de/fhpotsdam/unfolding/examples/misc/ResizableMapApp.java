package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Map will be resized when user drags the resize handle. This only works for Applications, not Applets.
 * 
 * The map will be resized, but everything else stays the same, i.e. even the current transformation center will be
 * consistent. If the frame size was increased, the missing tiles will be loaded, and added automatically.
 */
public class ResizableMapApp extends PApplet {

	UnfoldingMap map;

	float oldWidth;
	float oldHeight;
	
	public static boolean isApplet = true;

	public void setup() {
		if (isApplet) {
			println("This only works for Applications!");
		}
		size(800, 600, OPENGL);
		frame.setResizable(true);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
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

		// Random resizing to reproduce https://github.com/tillnagel/unfolding/issues/55
		// width = (int) random(screenWidth);
		// height = (int) random(screenHeight);
	}

	public static void main(String[] args) {
		isApplet = false;
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.misc.ResizableMapApp" });
	}
}

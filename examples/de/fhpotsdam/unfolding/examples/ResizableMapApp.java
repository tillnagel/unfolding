package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Map will be resized when user drags the resize handle. The map will be resized, but everything
 * else stays the same, i.e. even the current transformation center will be consistent. If the frame
 * size was increased, the missing tiles will be loaded, and added automatically.
 */
public class ResizableMapApp extends PApplet {

	Map map;

	float oldWidth;
	float oldHeight;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		frame.setResizable(true);

		map = new Map(this);
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
		ellipse(random(width), random(height), 50, 50);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ResizableMapApp" });
	}

}

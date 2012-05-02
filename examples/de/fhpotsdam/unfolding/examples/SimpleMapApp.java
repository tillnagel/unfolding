package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This demonstrates how to create a simple application with Unfolding.
 *
 * You get a map in which you can do the things you expect: move, zoom, etc.
 *
 * In order to get an Unfolding app your main class needs to inherit from
 * PApplet and implement setup() and draw().
 */
public class SimpleMapApp extends PApplet {

	/**
	 * Map is the main concept of Unfolding. It represents a map and offers lots
	 * of functionality: zooming, panning, moving, translating cursor positions
	 * to geo positions (Location), translating Location to cursor position, and
	 * much more.
	 */
	Map map;

	/**
	 * setup() gets called at the beginning. Here you shoud setup/initialize
	 * your Unfolding stuff.
	 */
	public void setup() {
		// set the display to 800x600 and use OpenGL acceleration
		size(800, 600, GLConstants.GLGRAPHICS);
		// if your computer does not offer OpenGL acceleration use the following
		// line instead
		//size(800, 600);

		// Here we actually create a the map and pass this. This way map knows
		// where to
		// draw on
		map = new Map(this);

		// Then we zoom to a Location with given latitude and longtitude
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

		// In order to have the expected behavior (movement and zooming with
		// mouse and keyboard)
		// default events get registered with the following function.
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	/**
	 * The draw function gets called periodically and needs to redraw.
	 */
	public void draw() {
		// Simply draw the map
		map.draw();
	}

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleMapApp" });
	}
}
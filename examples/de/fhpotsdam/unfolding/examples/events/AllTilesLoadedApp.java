package de.fhpotsdam.unfolding.examples.events;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple example showing how to react to the map being fully loaded.
 * 
 * If after a map manipulation (zoom, pan, provider switch, etc) new tiles have to be loaded, the tilesLoaded method
 * will be called automatically in your sketch if it exists. In that method you can do whatever you want, e.g. create
 * snapshots, or simply allow further interaction.
 */
public class AllTilesLoadedApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		map.draw();
	}

	/**
	 * Is called when all tiles of the map are loaded.
	 */
	public void tilesLoaded() {
		println("All tiles loaded.");
	}

}
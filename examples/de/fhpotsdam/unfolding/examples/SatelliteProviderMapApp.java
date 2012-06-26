package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Show how to use the Microsoft Aerial Tile Provider.
 */
public class SatelliteProviderMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(400, 400, GLConstants.GLGRAPHICS);
		// use MS ArialProvider
		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SatelliteProviderMapApp" });
	}
}
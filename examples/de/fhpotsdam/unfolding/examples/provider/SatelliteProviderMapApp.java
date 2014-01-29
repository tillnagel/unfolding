package de.fhpotsdam.unfolding.examples.provider;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Show how to use the Microsoft Aerial Tile Provider.
 */
public class SatelliteProviderMapApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.provider.SatelliteProviderMapApp" });
	}
}
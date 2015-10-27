package de.fhpotsdam.unfolding.examples.provider;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This example uses a local MBTiles file. Thus, it does not need an Internet connection to load tiles.
 * 
 * For testing purposes and to keep the file size small, this example supports only three zoom levels.
 */
public class MBTilesMapApp extends PApplet {

	// relative path varies depending on whether this was started as applet or application
	public static final String TILES_LOCATION_APPLICATION = "./bin/tiles/blankLight-1-3.mbtiles";
	public static final String TILES_LOCATION_APPLET = "./tiles/blankLight-1-3.mbtiles";

	public static String mbTilesString = TILES_LOCATION_APPLET;

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, new MBTilesMapProvider(mbTilesString));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(1, 3);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String[] args) {
		mbTilesString = TILES_LOCATION_APPLICATION;
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.provider.MBTilesMapApp" });
	}
}
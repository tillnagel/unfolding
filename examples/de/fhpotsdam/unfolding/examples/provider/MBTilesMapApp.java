package de.fhpotsdam.unfolding.examples.provider;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This app uses a local MBTiles file. Thus, it does not need an Internet connection to load tiles.
 * 
 * For testing purposes and to keep the file size small, this example supports only three zoom levels.
 */
public class MBTilesMapApp extends PApplet {

	// relative path varies depending on whether this was started as applet or application
	public static final String JDBC_CONN_STRING_APPLICATION = "jdbc:sqlite:./data/muse-dark-2-4.mbtiles";
	public static final String JDBC_CONN_STRING_APPLET = "jdbc:sqlite:../data/muse-dark-2-4.mbtiles";
	
	public static String mbTilesConnectionString = JDBC_CONN_STRING_APPLET;

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new MBTilesMapProvider(mbTilesConnectionString));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(2, 4);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String[] args) {
		mbTilesConnectionString = JDBC_CONN_STRING_APPLICATION;
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.MBTilesMapApp" });
	}
}
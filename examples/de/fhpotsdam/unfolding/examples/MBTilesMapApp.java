package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Use the TileMil Provider which does not need a internet connections to load
 * tiles.
 * 
 * Make sure sqlite works first!
 */
public class MBTilesMapApp extends PApplet {

	// Connection to SQLite/MBTiles in distribution (outside of the jar)
	public static final String JDBC_CONN_STRING_TABLE = "jdbc:sqlite:./data/muse-dark-2-4.mbtiles";
	// Connection to SQLite/MBTiles in dev environment (link to the project)
	public static final String JDBC_CONN_STRING_MAC = "jdbc:sqlite:../../unfolding/data/muse-dark-2-4.mbtiles";

	UnfoldingMap map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, 0, 0, width, height, new MBTilesMapProvider(
				JDBC_CONN_STRING_MAC));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(2, 8);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.MBTilesMapApp" });
	}
}
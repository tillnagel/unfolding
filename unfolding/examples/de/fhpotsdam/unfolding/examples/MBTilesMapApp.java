package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MBTilesMapApp extends PApplet {

	// Connection to SQLite/MBTiles in distribution (outside of the jar)
	public static final String JDBC_CONN_STRING_TABLE = "jdbc:sqlite:./data/muse-dark-2-8.mbtiles";
	// Connection to SQLite/MBTiles in dev environemtn (link to the project)
	public static final String JDBC_CONN_STRING_MAC = "jdbc:sqlite:../../unfolding/data/muse-dark-2-8.mbtiles";

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, 0, 0, width, height, new MBTilesMapProvider(JDBC_CONN_STRING_MAC));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(2, 9);
	}

	public void draw() {
		background(0);
		
		map.draw();
	}

}

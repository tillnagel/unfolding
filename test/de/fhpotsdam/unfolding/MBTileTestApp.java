package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.tiles.MBTilesLoaderUtils;

public class MBTileTestApp extends PApplet {

	// Connection to SQLite/MBTiles in distribution (outside of the jar)
	public static final String JDBC_CONN_STRING_TABLE = "jdbc:sqlite:./data/muse-dark-2-8.mbtiles";
	// Connection to SQLite/MBTiles in dev environemtn (link to the project)
	public static final String JDBC_CONN_STRING_MAC = "jdbc:sqlite:../../unfolding/data/muse-dark-2-8.mbtiles";

	PImage tile;
	
	public void setup() {
		size(600, 600);
		
		tile = MBTilesLoaderUtils.getMBTile(15, 10, 4, JDBC_CONN_STRING_MAC);
	}
	
	public void draw() {
		background(240);
		
		image(tile, mouseX, mouseY);
		
	}
	
}

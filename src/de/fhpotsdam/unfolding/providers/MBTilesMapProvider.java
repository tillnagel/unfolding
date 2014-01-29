package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;
import de.fhpotsdam.unfolding.tiles.MBTilesLoaderUtils;

/**
 * MapProvider for local MBTiles.
 */
public class MBTilesMapProvider extends AbstractMapTileProvider {
	
	private static final String JDBC_PREFIX = "jdbc:sqlite:";
	protected String jdbcConnectionString;

	public MBTilesMapProvider() {
		super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
				-1.068070890e7, 3.355443057e7)));
	}

	public MBTilesMapProvider(String jdbcConnectionString) {
		this();
		if (jdbcConnectionString != null && !jdbcConnectionString.startsWith(JDBC_PREFIX)) {
			jdbcConnectionString = JDBC_PREFIX + jdbcConnectionString;
		}
		this.jdbcConnectionString = jdbcConnectionString;
	}

	public int tileWidth() {
		return 256;
	}

	public int tileHeight() {
		return 256;
	}

	@Override
	public PImage getTile(Coordinate coord) {
		float gridSize = PApplet.pow(2, coord.zoom);
		float negativeRow = gridSize - coord.row - 1;

		return MBTilesLoaderUtils.getMBTile((int) coord.column, (int) negativeRow, (int) coord.zoom, jdbcConnectionString);
	}

}

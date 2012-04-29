package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;
import de.fhpotsdam.unfolding.tiles.MBTilesLoaderUtils;

/**
 * MapProvider for MBTiles.
 */
public class MBTilesMapProvider extends AbstractMapTileProvider {
	
	protected String jdbcConnectionString;

	public MBTilesMapProvider() {
		super(new MercatorProjection(26, new Transformation(1.068070779e7f, 0.0f, 3.355443185e7f, 0.0f,
				-1.068070890e7f, 3.355443057e7f)));
	}

	public MBTilesMapProvider(String jdbcConnectionString) {
		this();
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

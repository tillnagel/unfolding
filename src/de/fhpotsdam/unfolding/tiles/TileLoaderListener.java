package de.fhpotsdam.unfolding.tiles;

import de.fhpotsdam.unfolding.core.Coordinate;

/**
 * After the TileLoader has loaded an image, the tileLoaded method of this listener is called.
 */
public interface TileLoaderListener {

	public void tileLoaded(Coordinate coord, Object image);

}

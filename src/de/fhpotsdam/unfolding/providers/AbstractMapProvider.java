package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.AbstractProjection;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Abstract map provider handles map tiles in combination with the appropriate TileLoader.
 */
public abstract class AbstractMapProvider {

	public AbstractProjection projection;
	
	public boolean enableCache = false;

	public AbstractMapProvider(AbstractProjection projection) {
		this.projection = projection;
	}
	
	/**
	 * Gets tiles for coordinate.
	 * 
	 * Either this or @{link #getTileUrls(Coordinate)} has to be implemented.
	 * 
	 * @param coordinate
	 *            The position and zoom to get tile for.
	 * @return A single PImage with the tile.
	 */
	public abstract PImage getTile(Coordinate coordinate);

	/**
	 * Gets tile URLs for coordinate. May return multiple URLs, if provider handles multiple layers.
	 * 
	 * Either this or @{link #getTile(Coordinate)} has to be implemented.
	 * 
	 * @param coordinate
	 *            The position and zoom to get tile for.
	 * @return An array with tile URLs (mostly just one)
	 */
	public abstract String[] getTileUrls(Coordinate coordinate);

	public abstract int tileWidth();

	public abstract int tileHeight();
	

	public Coordinate locationCoordinate(Location location) {
		return projection.locationCoordinate(location);
	}

	public Location coordinateLocation(Coordinate coordinate) {
		return projection.coordinateLocation(coordinate);
	}

	public Coordinate sourceCoordinate(Coordinate coordinate) {
		float gridSize = PApplet.pow(2, coordinate.zoom);

		float wrappedColumn = coordinate.column % gridSize;
		while (wrappedColumn < 0) {
			wrappedColumn += gridSize;
		}

		float wrappedRow = coordinate.row % gridSize;
		while (wrappedRow < 0) {
			wrappedRow += gridSize;
		}

		return new Coordinate(wrappedRow, wrappedColumn, coordinate.zoom);
	}

	/** since we're often given four tile servers to pick from */
	public static float random(int lower, int higher) {
		return (float) ((double) lower + Math.random() * (double) (higher - lower));
	}

}
